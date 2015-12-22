package com.despegar.hestia.recommend.allinone.item.closedpackage;

import org.junit.Assert;

import org.junit.Test;

import ar.com.despegar.p13n.hestia.TestFuntions;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.ClosedPackagesDestinationItem;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.item.closedpackage.BuyClosedPackageItemStep;


public class BuyClosedPackageItemStepTest {

    private BuyClosedPackageItemStep steps;

    @Test
    public void testGetItemBuy() {
        this.steps = new BuyClosedPackageItemStep();
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.CLOSED_PACKAGES, "BUE");
        action.setOrigin("RIO");
        ClosedPackagesDestinationItem item = (ClosedPackagesDestinationItem) this.steps.execute("MIA", action);
        Assert.assertNotNull(item);
        Assert.assertTrue(item.getDestination().equals("MIA"));
        Assert.assertTrue(item.getOrigin().equals("RIO"));
        action.setOrigin("BUE");
        item = (ClosedPackagesDestinationItem) this.steps.execute("MDQ", action);
        Assert.assertNotNull(item);
        Assert.assertTrue(item.getDestination().equals("MDQ"));
        Assert.assertTrue(item.getOrigin().equals("BUE"));
    }

}
