package com.despegar.p13n.hestia.recommend.allinone.item.cruise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.BuyActivity;
import com.despegar.p13n.hestia.recommend.allinone.item.ItemStep;

@Component
public class CruiseItemSteps
    implements ItemStep {

    private SearchCruiseStep searchCruiseStep;
    private BuyCruiseStep buyCruiseStep;

    @Autowired
    public CruiseItemSteps(SearchCruiseStep searchCruiseStep, BuyCruiseStep buyCruiseStep) {
        this.searchCruiseStep = searchCruiseStep;
        this.buyCruiseStep = buyCruiseStep;
    }

    @Override
    public ItemHome execute(String destination, ActionRecommendation action) {
        BuyActivity activity = action.getBuyActivity();
        if (activity == null) {
            return this.searchCruiseStep.execute(destination, action);
        } else {
            return this.buyCruiseStep.execute(destination, action);
        }
    }
}
