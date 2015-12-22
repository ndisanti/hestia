package com.despegar.p13n.hestia.recommend.allinone.item.flight;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.BuyActivity;
import com.despegar.p13n.hestia.recommend.allinone.item.ItemStep;

@Component
public class FlightItemSteps
    implements ItemStep {

    private SearchFlightStep searchFlightStep;
    private BuyFlightStep buyStep;

    @Autowired
    public FlightItemSteps(SearchFlightStep searchFlightStep, BuyFlightStep buyStep) {
        this.searchFlightStep = searchFlightStep;
        this.buyStep = buyStep;
    }

    @Override
    public ItemHome execute(String destination, ActionRecommendation action) {
        BuyActivity buyActivity = action.getBuyActivity();
        if (buyActivity == null) {
            return this.searchFlightStep.execute(destination, action);
        } else {
            return this.buyStep.execute(destination, action);
        }
    }
}
