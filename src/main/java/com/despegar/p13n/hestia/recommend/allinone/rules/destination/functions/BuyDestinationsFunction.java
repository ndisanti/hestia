package com.despegar.p13n.hestia.recommend.allinone.rules.destination.functions;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.list.SetUniqueList;
import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.recommendations.Recommendation;
import com.despegar.p13n.hestia.api.data.model.home.ItemTypeId;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.BaseFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.ItemIdFuncCode;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.ItemIdFunction;
import com.despegar.p13n.hestia.utils.ItemUtils;

/**
 * Función destino:
 * <ul>
 * <li>8 destinos de HOTELS/HOTELS THANKS/THANKS</li>
 * <li>Si hacen falta más destinos, usamos el ranking de nearby cities</li>
 * <li>Si igualmente necesitamos más, utilizamos el ranking por país y producto de la home</li>
 * </ul>
 */

@Service
public class BuyDestinationsFunction
    extends BaseFunction
    implements ItemIdFunction {

    private static final int DESTINATIONS_LIMIT = 12;

    @Override
    public List<String> getItemIds(ActionRecommendation action) {

        ItemUtils.checkActivityType(action.getActivityType(), ActivityType.BUY);

        List<String> destinations = new ArrayList<String>();
        String city = action.getBuyActivity().getActivity().getDestination();
        this.calculateDestinationsHotelsHotelsThaknsThanks(destinations, action, city);
        if (destinations.size() < DESTINATIONS_LIMIT) {
            destinations = SetUniqueList.decorate(destinations);
            Product product = action.getCurrentHome();
            this.calculateDestinationsHotelsHotelsSearchSearch(action, product, destinations, city);
            if (destinations.size() < DESTINATIONS_LIMIT) {
                this.fillDefaultDestinations(action, product, destinations, DESTINATIONS_LIMIT);
            }
        }
        return (destinations.size() < DESTINATIONS_LIMIT) ? null : destinations;
    }

    private void calculateDestinationsHotelsHotelsSearchSearch(ActionRecommendation action, Product product,
        List<String> destinations, String destination) {
        List<Recommendation> recommendations = this.getRecommendation().recommend(Product.HOTELS, Flow.SEARCH,
            Product.HOTELS, Flow.SEARCH, destination, DESTINATIONS_LIMIT - destinations.size());
        for (Recommendation rec : recommendations) {
            destinations.add(rec.getRecommendation());
        }
    }

    private void calculateDestinationsHotelsHotelsThaknsThanks(List<String> destinations, ActionRecommendation action,
        String destination) {
        List<Recommendation> recommendations = this.getRecommendation().recommend(Product.HOTELS, Flow.THANKS,
            Product.HOTELS, Flow.THANKS, destination, DESTINATIONS_LIMIT);
        for (Recommendation rec : recommendations) {
            destinations.add(rec.getRecommendation());
        }
    }

    @Override
    public ItemIdFuncCode getFunctionCode() {
        return ItemIdFuncCode.BUY_DESTINATIONS;
    }

    @Override
    public ItemTypeId getItemTypeId() {
        return ItemTypeId.DESTINATION;
    }

    @Override
    public String getDescription() {
        return "return 9 destinations for buyer user";
    }
}
