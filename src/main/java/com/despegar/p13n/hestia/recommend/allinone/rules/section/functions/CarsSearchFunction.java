package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.UserAction;
import com.despegar.p13n.euler.commons.client.model.data.CarData;
import com.despegar.p13n.euler.commons.client.model.data.ProductData;
import com.despegar.p13n.hestia.api.data.model.CarsLocationDto;
import com.despegar.p13n.hestia.api.data.model.DestinationPriceDTO;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.service.external.PublicCarsService;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.activity.SearchActivity;
import com.despegar.p13n.hestia.recommend.allinone.activity.UserActivity;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.BaseSectionFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;
import com.despegar.p13n.hestia.utils.ItemUtils;

/** 
 * <pre>
 * if Search activity for CARS 
 *      return ranking top for this car category
 * else
 *      return ranking for the most "checkouted" category
 * </pre>
 */
@Service
public class CarsSearchFunction
    extends BaseSectionFunction {

    @Autowired
    private PublicCarsService carsService;

    @Override
    public List<Offer> buildOffers(Product home, Product prToOffer, ActionRecommendation action, Param param) {

        ItemUtils.checkProduct(prToOffer, Product.CARS);
        ItemUtils.checkActivityType(action.getActivityType(), ActivityType.SEARCH);

        SearchActivity search = action.getSearchActivity();
        String destination = null;

        String category = null;

        if (search.isActivityFor(Product.CARS)) {
            UserActivity activity = search.getActivity(Product.CARS);
            UserAction userAction = activity.getAction();
            CarData carData = ProductData.create(userAction);
            category = carData.category();
            destination = activity.getDestination();
        } else {
            destination = search.getLastActivity().getDestination();
        }


        DestinationPriceDTO priceDto = this.getCategoryOrTop(action.getCountryCode(), destination, category);

        if (priceDto == null) {
            return null;
        }

        action.getTitleData().addDestination(destination);

        ItemHome itemHome = this.buildDestination(Product.CARS, null, priceDto.getPul(), null, action);

        return ItemUtils.buildOffers(home, itemHome, action, prToOffer);

    }


    private DestinationPriceDTO getCategoryOrTop(CountryCode cc, String pul, String category) {
        Map<String, CarsLocationDto> categories = this.carsService.getTopCategoriesByDestination(cc.toString(), pul);

        if (categories.isEmpty()) {
            return null;
        }

        if (category == null && categories.containsKey(category)) {


            return this.toDestinationPriceDto(categories.get(category));
        }

        return this.toDestinationPriceDto(categories.values().iterator().next());
    }

    private DestinationPriceDTO toDestinationPriceDto(CarsLocationDto dto) {
        return new DestinationPriceDTO(dto.getPul(), dto.getPultype(), "", BigDecimal.ZERO);
    }


    @Override
    public SectionFunctionCode getFunctionCode() {
        return SectionFunctionCode.CARS_SEARCH;
    }

    @Override
    public String getDescription() {
        return "Si busco CARS, ranking top para la categoria del auto en el destino. "
            + "Si no, ranking para la categoria mas checkouteada para el destino buscado";
    }
}
