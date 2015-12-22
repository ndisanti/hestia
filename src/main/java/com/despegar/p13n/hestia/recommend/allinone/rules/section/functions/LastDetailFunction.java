package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.HotelItem;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.activity.UserActivity;
import com.despegar.p13n.hestia.recommend.allinone.item.hotel.HotelItemBuilderCommonsFunctions;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.BaseSectionFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;
import com.despegar.p13n.hestia.utils.ItemUtils;

@Service
public class LastDetailFunction
    extends BaseSectionFunction {

    @Autowired
    private HotelItemBuilderCommonsFunctions commons;

    @Override
    public List<Offer> buildOffers(Product home, Product pr, ActionRecommendation action, Param param) {

        ItemUtils.checkProduct(pr, Product.HOTELS);
        ItemUtils.checkActivityType(action.getActivityType(), ActivityType.SEARCH, ActivityType.BUY);

        if (action.getSearchActivity() != null && action.getSearchActivity().isActivityFor(pr)) {

            UserActivity lastActivity = action.getSearchActivity().getActivity(pr);

            if (lastActivity.isDetailOrCheckout()) {
                action.getTitleData().addDestination(lastActivity.getDestination());
                String hid = lastActivity.getProductBusinessId();
                HotelItem item = new HotelItem(hid);
                return this.commons.isHotelAvailable(item) ? ItemUtils.buildOffers(home, item, action, pr) : null;
            }
        }
        return null;
    }

    @Override
    public SectionFunctionCode getFunctionCode() {
        return SectionFunctionCode.LAST_DETAIL_FUNCTION;
    }

    @Override
    public String getDescription() {
        return "Ultimo detail de hotel";
    }

}
