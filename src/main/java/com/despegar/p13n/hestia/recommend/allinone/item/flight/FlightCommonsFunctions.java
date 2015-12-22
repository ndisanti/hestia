package com.despegar.p13n.hestia.recommend.allinone.item.flight;

import org.springframework.stereotype.Component;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.FlightDestinationItem;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.BaseFunction;
import com.despegar.p13n.hestia.utils.ItemUtils;

@Component
public class FlightCommonsFunctions
    extends BaseFunction {

    public FlightDestinationItem builDestination(ActionRecommendation action, String origin, String destination) {

        FlightDestinationItem item = (FlightDestinationItem) this.buildDestination(Product.FLIGHTS, null, destination,
            origin, action);
        return (FlightDestinationItem) ItemUtils.checkUnique(action.getCurrentHome(), item, action, Product.FLIGHTS);
    }

    @Override
    public String getDescription() {
        return "Commons functions for FlghtItems steps";
    }



}
