package com.despegar.p13n.hestia.recommend.allinone.item.cruise;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.CruiseItem;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.UserActivity;
import com.despegar.p13n.hestia.recommend.allinone.item.ItemStep;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.BaseFunction;
import com.despegar.p13n.hestia.utils.ItemUtils;

@Component
public class SearchCruiseStep
    extends BaseFunction
    implements ItemStep {

    private CruiseCommonsFunctions commons;

    @Autowired
    public SearchCruiseStep(CruiseCommonsFunctions commons) {
        this.commons = commons;
    }

    @Override
    public ItemHome execute(String destination, ActionRecommendation action) {
        UserActivity cruiseActivity = action.getSearchActivity().getActivity(Product.CRUISES);
        if (cruiseActivity != null && cruiseActivity.getAction().getFlow().equals(Flow.DETAIL)) {
            return this.getItemSearchDetailStep1(destination, action);
        } else {
            return this.getItemSearchDefaultStep1(destination, action);
        }
    }

    private ItemHome getItemSearchDefaultStep1(String destination, ActionRecommendation action) {
        return this.commons.getItemFromRanking(destination, action);
    }

    private ItemHome getItemSearchDetailStep1(String destination, ActionRecommendation action) {
        return this.getViewedCruise(destination, action);
    }

    private ItemHome getViewedCruise(String destination, ActionRecommendation action) {
        CruiseItem item = null;
        Collection<String> dataList = action.getSearchActivity().getLastCruisesByDestination().get(destination);
        Iterator<String> it = dataList.iterator();
        boolean found = false;
        while (it.hasNext() && !found) {
            String cruise = it.next();
            item = new CruiseItem(cruise);
            if (ItemUtils.checkUnique(action.getCurrentHome(), item, action, Product.CRUISES) != null) {
                found = true;
            }
        }
        return found ? item : null;
    }

    @Override
    public String getDescription() {
        return "return CruiseItem for user that didn't buy";
    }

}
