package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.CarRankingPositionDTO;
import com.despegar.p13n.hestia.api.data.model.CarsLocationDto;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.PriceCarCategoryItem;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.api.service.external.PublicCarsService;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.IteratorAdapter;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.BaseSectionFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.CarsItemBuilder;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;
import com.despegar.p13n.hestia.utils.ItemUtils;
import com.google.common.base.Preconditions;

/**
 * Destination ranking with price by category
 * 
 * "Ranking de categoría en el destino que compró o busco"
 */
@Service
public class CarsCategoryDestinationFunction
    extends BaseSectionFunction {

    @Autowired
    private PublicCarsService carsService;

    private CarsCategoryBuilder itemBuilder = new CarsCategoryBuilder();

    @Override
    public List<Offer> buildOffers(Product home, Product prOffer, ActionRecommendation action, Param param) {

        ItemUtils.checkProduct(prOffer, Product.CARS);
        ItemUtils.checkActivityType(action.getActivityType(), ActivityType.SEARCH, ActivityType.BUY);

        List<CarRankingPositionDTO> list = this.getNormalizedCarRankingPositionDTOList(this.getRankingIterator(prOffer,
            action, param));
        return ItemUtils.buildOffersCars(home, prOffer, action, param, list, this.itemBuilder);

    }

    @Override
    public RowHome buildRow(Product home, Product prOffer, ActionRecommendation action, Param param) {

        ItemUtils.checkProduct(prOffer, Product.CARS);
        ItemUtils.checkActivityType(action.getActivityType(), ActivityType.SEARCH, ActivityType.BUY);

        List<CarRankingPositionDTO> list = this.getNormalizedCarRankingPositionDTOList(this.getRankingIterator(prOffer,
            action, param));
        return ItemUtils.buildRowCars(home, prOffer, action, param, list, this.itemBuilder);
    }

    private class CarsCategoryBuilder
        implements CarsItemBuilder {

        @Override
        public ItemHome buildItem(Product home, Product prOffer, ActionRecommendation action, Param param,
            CarRankingPositionDTO dto) {

            return new PriceCarCategoryItem(dto.getPul(), dto.getPultype(), dto.getCarcat(), 0, "");
        }

    }

    private Iterator<CarRankingPositionDTO> getRankingIterator(Product prOffer, final ActionRecommendation action,
        Param param) {

        final String cc = action.getCountryCode().toString();
        final String destination = action.getLastDestination();

        Preconditions.checkNotNull(cc);
        Preconditions.checkNotNull(destination, "Destination is null");

        action.getTitleData().addDestination(destination);

        final Iterator<Entry<String, CarsLocationDto>> iterator = this.carsService
            .getTopCategoriesByDestination(cc, destination).entrySet().iterator();


        return new IteratorAdapter<Entry<String, CarsLocationDto>, CarRankingPositionDTO>(iterator) {

            @Override
            public CarRankingPositionDTO convert(Entry<String, CarsLocationDto> f) {
                CarsLocationDto dto = f.getValue();

                // TODO: we dont send price or currency
                return new CarRankingPositionDTO(dto.getPul(), dto.getPultype(), f.getKey(), 0, "");
            }
        }.newIterator();
    }

    @Override
    public SectionFunctionCode getFunctionCode() {
        return SectionFunctionCode.CARS_CATEGORY_DESTINATION;
    }

    @Override
    public String getDescription() {
        return "Ranking de categoría en el destino que compró o buscó";
    }
}
