package com.despegar.hestia.recommend.allinone.item.car;

import org.junit.Assert;

import org.junit.Test;

import ar.com.despegar.p13n.hestia.TestFuntions;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.CarDestinationItem;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.item.car.CarItemSteps;

public class CarItemStepsTest {

    @Test
    public void test() {
        CarItemSteps steps = new CarItemSteps();
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.CARS, "MIA");
        CarDestinationItem item = steps.execute("MIA", action);
        Assert.assertNotNull(item);
        Assert.assertTrue(item.getDestination().equals("MIA"));
        item = steps.execute("BUE", action);
        Assert.assertNotNull(item);
        Assert.assertTrue(item.getDestination().equals("BUE"));
    }

}
