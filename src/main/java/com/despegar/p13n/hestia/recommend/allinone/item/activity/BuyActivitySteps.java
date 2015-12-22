package com.despegar.p13n.hestia.recommend.allinone.item.activity;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.recommendations.Recommendation;
import com.despegar.p13n.hestia.api.data.model.ActivityItem;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.BuyActivity;
import com.despegar.p13n.hestia.recommend.allinone.item.ItemStep;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.BaseFunction;
import com.despegar.p13n.hestia.utils.ItemUtils;

@Component
public class BuyActivitySteps
    extends BaseFunction
    implements ItemStep {

	@Autowired
    private ActivityStepsCommonsFunctions commons;

    @Override
    public ItemHome execute(String destination, ActionRecommendation action) {
        BuyActivity activity = action.getBuyActivity();
        ActivityItem item = null;
        if (activity.getProduct().equals(Product.ACTIVITIES)) {
            item = this.getRecommendationDetailDetail(action);
        }
        return (item == null) ? this.commons.getItemFromRanking(action, destination) : item;

    }

    private ActivityItem getRecommendationDetailDetail(ActionRecommendation action) {
        String itemId = action.getBuyActivity().getActivity().getProductBusinessId();
        Iterator<Recommendation> it = this.getRecommendation()
            .recommend(Product.ACTIVITIES, Flow.DETAIL, Product.ACTIVITIES, Flow.DETAIL, itemId, RANKING_SIZE).iterator();
        boolean found = false;
        ActivityItem item = null;
        while (it.hasNext() && !found) {
            Recommendation pr = it.next();
            String id = pr.getRecommendation();
            item = new ActivityItem(id);
            if (ItemUtils.checkUnique(action.getCurrentHome(), item, action, Product.ACTIVITIES) != null) {
                found = true;
            }
        }
        return found ? item : null;
    }

    @Override
    public String getDescription() {
        return "return a ActivityItem for purchaser";
    }

}
