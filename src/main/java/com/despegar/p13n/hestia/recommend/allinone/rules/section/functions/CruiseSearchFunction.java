package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import java.util.List;

import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.data.CruiseData;
import com.despegar.p13n.euler.commons.client.model.data.ProductData;
import com.despegar.p13n.hestia.api.data.model.CruiseItem;
import com.despegar.p13n.hestia.api.data.model.CruiseRegionItem;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.activity.UserActivity;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.BaseSectionFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;
import com.despegar.p13n.hestia.utils.ItemUtils;

/**
 * Last cruise did from detail, or last region from destination 
 */
@Service
public class CruiseSearchFunction
    extends BaseSectionFunction {

    @Override
    public List<Offer> buildOffers(Product home, Product pr, ActionRecommendation action, Param param) {

        ItemUtils.checkProduct(pr, Product.CRUISES);
        ItemUtils.checkActivityType(action.getActivityType(), ActivityType.SEARCH, ActivityType.BUY);

        // there is no Search for Cruises, so we default...
        if (!action.getSearchActivity().isActivityFor(Product.CRUISES)) {
            return null;
        }

        UserActivity activity = action.getSearchActivity().getActivity(Product.CRUISES);

        action.getTitleData().addDestination(activity.getDestination());

        if (activity.isDetailOrCheckout()) {
            return ItemUtils.buildOffers(home, new CruiseItem(activity.getProductBusinessId()), action, pr);
        } else { // SEARCH flow
            CruiseData cruiseData = ProductData.create(activity.getAction());
            return ItemUtils.buildOffers(home, new CruiseRegionItem(cruiseData.region()), action, pr);
        }
    }

    @Override
    public SectionFunctionCode getFunctionCode() {
        return SectionFunctionCode.CRUISE_SEARCH;
    }

    @Override
    public String getDescription() {
        return "Ultimo did del crucero en detail, o ultima region buscada."//
            + "Si no busco Cruceros, defaultea";
    }
}