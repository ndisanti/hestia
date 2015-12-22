package com.despegar.p13n.hestia.recommend.allinone.item.activity;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.ActivityDestinationItem;
import com.despegar.p13n.hestia.api.data.model.ActivityItem;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.UserActivity;
import com.despegar.p13n.hestia.recommend.allinone.item.ItemStep;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.BaseFunction;
import com.despegar.p13n.hestia.utils.ItemUtils;

@Component
public class SearchActivitySteps
    extends BaseFunction
    implements ItemStep {

    private ActivityStepsCommonsFunctions commons;

    @Autowired
    public SearchActivitySteps(ActivityStepsCommonsFunctions commons) {
        this.commons = commons;
    }

    @Override
    public ItemHome execute(String destination, ActionRecommendation action) {
        if (destination.equals("ORL") || destination.equals("MIA")) {
            return this.getItemForOrlMiaStep1(action, destination);
        } else {
            return this.getItemDefaultStep1(destination, action);
        }
    }

    private ItemHome getItemDefaultStep1(String destination, ActionRecommendation action) {
        ItemHome item;
        UserActivity userActivity = action.getSearchActivity().getActivity(Product.ACTIVITIES);
        if (userActivity != null && userActivity.getAction().getFlow().equals(Flow.DETAIL)) {
            item = this.getViewedActivity(destination, action);
        } else {
            item = this.getActivityDestinationItem(destination, action);
        }
        return (item == null) ? this.getItemDefaultStep2(destination, action) : item;
    }

    private ActivityDestinationItem getActivityDestinationItem(String destination, ActionRecommendation action) {
        ActivityDestinationItem item = (ActivityDestinationItem) this.buildDestination(Product.ACTIVITIES, null,
            destination, action.getOrigin(), action);
        return (ActivityDestinationItem) ItemUtils.checkUnique(action.getCurrentHome(), item, action, Product.ACTIVITIES);
    }

    private ItemHome getItemDefaultStep2(String destination, ActionRecommendation action) {
        return this.commons.getItemFromRanking(action, destination);
    }

    private ActivityItem getItemForOrlMiaStep1(ActionRecommendation action, String destination) {
        ActivityItem item;
        item = new ActivityItem("DY_ORL");
        item = (ActivityItem) ItemUtils.checkUnique(action.getCurrentHome(), item, action, Product.ACTIVITIES);
        return (item == null) ? this.getItemForOrlMiaStep2(destination, action) : item;
    }


    private ActivityItem getItemForOrlMiaStep2(String destination, ActionRecommendation action) {
        ActivityItem item = new ActivityItem("UN_ORL");
        item = (ActivityItem) ItemUtils.checkUnique(action.getCurrentHome(), item, action, Product.ACTIVITIES);
        return (item == null) ? this.getItemForOrlMiaStep3(destination, action) : item;
    }

    private ActivityItem getItemForOrlMiaStep3(String destination, ActionRecommendation action) {
        ActivityItem item = this.getViewedActivity(destination, action);
        return (item == null) ? this.getItemForOrlMiaStep4(destination, action) : item;
    }

    private ActivityItem getItemForOrlMiaStep4(String destination, ActionRecommendation action) {
        return this.commons.getItemFromRanking(action, destination);
    }

    public ActivityItem getViewedActivity(String destination, ActionRecommendation action) {
        ActivityItem item = null;
        Collection<String> dataList = action.getSearchActivity().getLastActivitiesByDestination().get(destination);
        Iterator<String> it = dataList.iterator();
        boolean found = false;
        while (it.hasNext() && !found) {
            String ac = it.next();
            item = new ActivityItem(ac);
            if (ItemUtils.checkUnique(action.getCurrentHome(), item, action, Product.ACTIVITIES) != null) {
                found = true;
            }
        }
        return found ? item : null;
    }

    @Override
    public String getDescription() {
        return "return a ActivityItem or ActivityDestinationItem for users that didn't buy";
    }

}
