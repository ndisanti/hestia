package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.data.CruiseData;
import com.despegar.p13n.euler.commons.client.model.data.ProductData;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RankingItemDTO;
import com.despegar.p13n.hestia.data.hbase.hot.types.StaticRankingTypes;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.BaseSectionFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;
import com.despegar.p13n.hestia.utils.ItemUtils;
import com.google.common.base.Preconditions;

/**
 * @author sebastian
 *
 *<pre>
 *  User        Orign       Destination     Flight
 *  A           A           A               ANY
 *  A           A           B               B-A
 *  A           B           B               A-B
 *  A           B           C               A-B
 *  A           B           A               A-B  
 *</pre>
 */
@Service
public class CruiseBuyOfferFlightsFunction
    extends BaseSectionFunction {

    @Override
    public List<Offer> buildOffers(Product home, Product pr, ActionRecommendation action, Param param) {

        ItemUtils.checkProduct(pr, Product.FLIGHTS);
        ItemUtils.checkActivityType(action.getActivityType(), ActivityType.BUY);
        Preconditions.checkArgument(action.getBuyActivity().getProduct() == Product.CRUISES);

        CruiseData cruiseData = ProductData.create(action.getBuyActivity().getActivity().getAction());

        String cruiseOrigin = cruiseData.getItinerary().get(0);
        String cruiseDestination = cruiseData.getItinerary().get(cruiseData.getItinerary().size() - 1);

        String origin = action.getOrigin();

        ItemHome flightDestination = null;

        if (origin.equals(cruiseOrigin)) {

            if (!cruiseOrigin.equals(cruiseDestination)) {// A - A - B
                flightDestination = this.buildFlights(cruiseDestination, origin, action);
            }

        } else {
            // A - B - B
            // A - B - C
            // A - B - A
            flightDestination = this.buildFlights(origin, cruiseOrigin, action);
        }


        // handling:
        // A - A - A
        // or origin matches destination

        if (flightDestination == null) {
            Iterator<RankingItemDTO> iterator = this.getRankingIterator(pr, action,
                StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_ANY);

            while (iterator.hasNext() && flightDestination == null) {
                RankingItemDTO itemDTO = iterator.next();

                String destination = itemDTO.getId();
                flightDestination = this.buildFlights(origin, destination, action);
            }
        }
        return ItemUtils.buildOffers(home, flightDestination, action, pr);
    }

    private ItemHome buildFlights(String origin, String destination, ActionRecommendation action) {
        return CruiseBuyOfferFlightsFunction.this.buildDestination(Product.FLIGHTS, null, destination, origin, action);
    }

    @Override
    public SectionFunctionCode getFunctionCode() {
        return SectionFunctionCode.CRUISE_BUY_OFFER_FLIGHTS;
    }

    @Override
    public String getDescription() {
        return "Ofrece flights cuando se compro un crucero, en base al origin del usuario, origin del crucero y el destino del crucero";
    }


}
