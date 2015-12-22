package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import java.util.List;

import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.data.ClosedPackageData;
import com.despegar.p13n.euler.commons.client.model.data.ProductData;
import com.despegar.p13n.hestia.api.data.model.ClosedPackagesDestinationItem;
import com.despegar.p13n.hestia.api.data.model.ClosedPackagesItem;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.activity.UserActivity;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.BaseSectionFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;
import com.despegar.p13n.hestia.utils.ItemUtils;

/**
 * Last seen packages or package in the destination that saw. 
 */
@Service
public class PackageSearchFunction
    extends BaseSectionFunction {

    @Override
    public List<Offer> buildOffers(Product home, Product pr, ActionRecommendation action, Param param) {

        ItemUtils.checkProduct(pr, Product.CLOSED_PACKAGES);
        ItemUtils.checkActivityType(action.getActivityType(), ActivityType.SEARCH);

        UserActivity activity = action.getSearchActivity().getLastActivity();
        action.getTitleData().addDestination(activity.getDestination());

        if (activity.isDetailOrCheckout() && activity.getAction().getProduct() == Product.CLOSED_PACKAGES) {
            ClosedPackageData cpd = ProductData.create(activity.getAction());
            return ItemUtils.buildOffers(home, new ClosedPackagesItem(cpd.clusterId()), action, pr);

        } else { // other product detail or search
            if (ItemUtils.isOriginOk(pr, action.getOrigin(), activity.getDestination())) {
                return ItemUtils.buildOffers(home,
                    new ClosedPackagesDestinationItem(activity.getDestination(), action.getOrigin()), action, pr);
            } else {
                return null;
            }
        }
    }

    @Override
    public SectionFunctionCode getFunctionCode() {
        return SectionFunctionCode.PACKAGE_SEARCH;
    }

    @Override
    public String getDescription() {
        return "Ultimo package visto o package en el destino que vio";
    }

}
