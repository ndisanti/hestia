package com.despegar.p13n.hestia.recommend.allinone.item.flight;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.item.ItemStep;

@Component
public class SearchFlightStep
    implements ItemStep {

    FlightCommonsFunctions commons;

    @Autowired
    public SearchFlightStep(FlightCommonsFunctions commons) {
        this.commons = commons;
    }

    public ItemHome execute(String destination, ActionRecommendation action) {
        return this.commons.builDestination(action, action.getOrigin(), destination);
    }
}
