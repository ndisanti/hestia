package com.despegar.p13n.hestia.recommend.allinone.item.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.BuyActivity;
import com.despegar.p13n.hestia.recommend.allinone.item.ItemStep;

@Component
public class ActivityItemSteps
    implements ItemStep {


    private SearchActivitySteps searchActivitySteps;
    private BuyActivitySteps buyActivitySteps;

    @Autowired
    public ActivityItemSteps(SearchActivitySteps searchActivitySteps, BuyActivitySteps buyActivitySteps) {
        this.searchActivitySteps = searchActivitySteps;
        this.buyActivitySteps = buyActivitySteps;
    }

    @Override
    public ItemHome execute(String destination, ActionRecommendation action) {

        BuyActivity activity = action.getBuyActivity();
        if (activity == null) {
            return this.searchActivitySteps.execute(destination, action);
        } else {
            return this.buyActivitySteps.execute(destination, action);
        }
    }

}
