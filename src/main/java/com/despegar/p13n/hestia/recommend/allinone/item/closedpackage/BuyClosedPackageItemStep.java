package com.despegar.p13n.hestia.recommend.allinone.item.closedpackage;

import org.springframework.stereotype.Component;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.item.ItemStep;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.BaseFunction;
import com.despegar.p13n.hestia.utils.ItemUtils;

@Component
public class BuyClosedPackageItemStep
    extends BaseFunction
    implements ItemStep {

    @Override
    public ItemHome execute(String destination, ActionRecommendation action) {
        ItemHome item = this.buildDestination(Product.CLOSED_PACKAGES, null, destination, action.getOrigin(), action);
        return ItemUtils.checkUnique(action.getCurrentHome(), item, action, Product.CLOSED_PACKAGES);
    }

    @Override
    public String getDescription() {
        return "return a ClosedPackagesItem for purchaser";
    }

}
