package com.despegar.p13n.hestia.recommend.allinone.item.cruise;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.recommendations.Recommendation;
import com.despegar.p13n.hestia.api.data.model.CruiseItem;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.BuyActivity;
import com.despegar.p13n.hestia.recommend.allinone.item.ItemStep;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.BaseFunction;
import com.despegar.p13n.hestia.utils.ItemUtils;

@Component
public class BuyCruiseStep
    extends BaseFunction
    implements ItemStep {

	@Autowired
    private CruiseCommonsFunctions commons;

    @Override
    public ItemHome execute(String destination, ActionRecommendation action) {
        BuyActivity activity = action.getBuyActivity();
        if (activity.getProduct().equals(Product.CRUISES)) {
            return this.getItemStep1(action, destination);
        } else {
            return this.getItemDefaultStep1(destination, action);
        }
    }

    private ItemHome getItemDefaultStep1(String destination, ActionRecommendation action) {
        return this.commons.getItemFromRanking(destination, action);
    }

    private ItemHome getItemStep1(ActionRecommendation action, String destination) {
        ItemHome item = this.getRecommendationDetailDetail(action);
        return (item == null) ? this.getItemStep2(destination, action) : item;
    }

    private ItemHome getItemStep2(String destination, ActionRecommendation action) {
        return this.commons.getItemFromRanking(destination, action);
    }


    private ItemHome getRecommendationDetailDetail(ActionRecommendation action) {

        String itemId = action.getBuyActivity().getActivity().getProductBusinessId();
        Iterator<Recommendation> it = this.getRecommendation()
            .recommend(Product.CRUISES, Flow.DETAIL, Product.CRUISES, Flow.DETAIL, itemId, RANKING_SIZE).iterator();
        boolean found = false;
        CruiseItem item = null;
        while (it.hasNext() && !found) {
            Recommendation pr = it.next();
            String id = pr.getRecommendation();
            item = new CruiseItem(id);
            if (ItemUtils.checkUnique(action.getCurrentHome(), item, action, Product.CRUISES) != null) {
                found = true;
            }
        }
        return found ? item : null;
    }

    @Override
    public String getDescription() {
        return "return a CruiseItem for purchaser";
    }

}
