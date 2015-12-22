package com.despegar.p13n.hestia.recommend.allinone.item.flight;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.BuyActivity;
import com.despegar.p13n.hestia.recommend.allinone.item.ItemStep;

@Component
public class BuyFlightStep
    implements ItemStep {

    FlightCommonsFunctions commons;

    @Autowired
    public BuyFlightStep(FlightCommonsFunctions commons) {
        this.commons = commons;
    }

    @Override
    public ItemHome execute(String destination, ActionRecommendation action) {
        BuyActivity buyActivity = action.getBuyActivity();

        if (buyActivity.getProduct().equals(Product.FLIGHTS) || //
            buyActivity.getProduct().equals(Product.CLOSED_PACKAGES) || //
            buyActivity.getProduct().equals(Product.COMBINED_PRODUCTS)) {
            String origin = buyActivity.getActivity().getDestination();
            return this.getItemStep1(destination, action, origin);
        } else {
            return this.getItemDefaultStep1(destination, action);
        }

    }

    private ItemHome getItemDefaultStep1(String destination, ActionRecommendation action) {
        return this.commons.builDestination(action, action.getOrigin(), destination);
    }

    private ItemHome getItemStep1(String destination, ActionRecommendation action, String origin) {
        return this.commons.builDestination(action, origin, destination);
    }

}
