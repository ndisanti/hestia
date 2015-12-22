package com.despegar.p13n.hestia.recommend.allinone.item.closedpackage;

import org.springframework.stereotype.Component;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.ClosedPackagesDestinationItem;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.item.ItemStep;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.BaseFunction;
import com.despegar.p13n.hestia.utils.ItemUtils;

// Esta clase es temporal hasta que front pueda soportar paquetes
@Component
public class TemporaryClosedPackagesSteps
    extends BaseFunction
    implements ItemStep {

    @Override
    public ClosedPackagesDestinationItem execute(String destination, ActionRecommendation action) {
        ClosedPackagesDestinationItem item = (ClosedPackagesDestinationItem) this.buildDestination(Product.CLOSED_PACKAGES,
            null, destination, action.getOrigin(), action);
        return (ClosedPackagesDestinationItem) ItemUtils.checkUnique(action.getCurrentHome(), item, action,
            Product.CLOSED_PACKAGES);
    }

    @Override
    public String getDescription() {
        return "return ClosedPackagesDestinationItem";
    }


}
