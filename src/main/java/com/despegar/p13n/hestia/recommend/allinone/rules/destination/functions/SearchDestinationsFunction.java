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
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.BaseFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.ItemIdFuncCode;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.ItemIdFunction;
import com.google.common.collect.Iterables;

/**
 * Función destino:
 * <ul>
 * <li>Deberia devolver 10 destinos </li>
 * <li>2 destinos más importantes del producto de la home actual</li>
 * <li>10 destinos del recomendador SEARCH/SEARCH sobre el destino que sale de</li>
 * <li>Si faltan más destinos usamos el recomendador sobre el 2do destino más importante</li>
 * <li>Si igualmente necesitamos más, utilizamos el ranking por país y producto de la home</li>
 * </ul>
 *
 */
@Service
public class SearchDestinationsFunction
    extends BaseFunction
    implements ItemIdFunction {

    private static final int DESTINATIONS_LIMIT = 10;

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getItemIds(ActionRecommendation action) {

        Product product = action.getCurrentHome();
        List<String> destinations = new ArrayList<String>();
        destinations = SetUniqueList.decorate(destinations);
        this.checkLastDestinations(product, destinations, action);
        // el recomendador no esta configurado para HOME_AS_PRODUCT-SEARCH-HOME_AS_PRODUCT-SEARCH
        product = product.equals(Product.HOME_AS_PRODUCT) ? Product.HOTELS : product;
        // el recomendador no esta configurado para INSURANCE-SEARCH-INSURANCE-SEARCH
        product = product.equals(Product.INSURANCE) ? Product.HOTELS : product;
        if (!product.equals(Product.CRUISES)) {
            // TODO el recomendador para cruceros recibe como imput regiones y devuelve regiones.
            this.checkDestinationsRecommendation(product, destinations);
        }
        if (destinations.size() < DESTINATIONS_LIMIT) {
            // TODO si es crucero utilizo ranking de hoteles
            product = product.equals(Product.CRUISES) ? Product.HOTELS : product;
            this.fillDefaultDestinations(action, product, destinations, DESTINATIONS_LIMIT);
        }
        return (destinations.size() < DESTINATIONS_LIMIT) ? null : destinations;
    }


    private void checkLastDestinations(Product product, List<String> destinations, ActionRecommendation action) {

        Iterable<String> dests = Iterables.limit(action.getSearchActivity().getLastDestinations(), 2);
        Iterables.addAll(destinations, dests);
    }

    private void checkDestinationsRecommendation(Product product, List<String> destinations) {
        int index = 0;
        int size = destinations.size();
        while (destinations.size() < DESTINATIONS_LIMIT && index < size) {
            this.checkDestinationsRecommendation(product, destinations.get(index), destinations);
            index++;
        }
    }

    private void checkDestinationsRecommendation(Product product, String destination, List<String> destinations) {

        List<Recommendation> recommendations = this.getRecommendation().recommend(product, Flow.SEARCH, product,
            Flow.SEARCH, destination, DESTINATIONS_LIMIT - destinations.size());
        for (Recommendation rec : recommendations) {
            destinations.add(rec.getRecommendation());
        }
    }

    @Override
    public String getDescription() {
        return "Return 10 destinations for search user. ";
    }

    @Override
    public ItemIdFuncCode getFunctionCode() {
        return ItemIdFuncCode.SEARCH_DESTINATIONS;
    }

    @Override
    public ItemTypeId getItemTypeId() {
        return ItemTypeId.DESTINATION;
    }

}
