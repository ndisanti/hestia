package com.despegar.p13n.hestia.recommend.allinone.item.car;

import org.springframework.stereotype.Component;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.CarDestinationItem;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.item.ItemStep;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.BaseFunction;
import com.despegar.p13n.hestia.utils.ItemUtils;

@Component
public class CarItemSteps
    extends BaseFunction
    implements ItemStep {

    @Override
    public CarDestinationItem execute(String destination, ActionRecommendation action) {
        CarDestinationItem item = (CarDestinationItem) this.buildDestination(Product.CARS, null, destination,
            action.getOrigin(), action);
        return (CarDestinationItem) ItemUtils.checkUnique(action.getCurrentHome(), item, action, Product.CARS);
    }

    @Override
    public String getDescription() {
        return "return CarDestination item";
    }


}
