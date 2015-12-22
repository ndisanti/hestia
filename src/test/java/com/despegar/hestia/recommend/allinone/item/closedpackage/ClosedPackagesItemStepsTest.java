package com.despegar.hestia.recommend.allinone.item.closedpackage;

import org.junit.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ar.com.despegar.p13n.hestia.TestFuntions;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.ClosedPackagesItem;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.item.closedpackage.BuyClosedPackageItemStep;
import com.despegar.p13n.hestia.recommend.allinone.item.closedpackage.ClosedPackagesItemSteps;
import com.despegar.p13n.hestia.recommend.allinone.item.closedpackage.SearchClosedPackageItemStep;

@RunWith(MockitoJUnitRunner.class)
public class ClosedPackagesItemStepsTest {

	@InjectMocks
    private ClosedPackagesItemSteps steps = new ClosedPackagesItemSteps();
    @Mock
    private SearchClosedPackageItemStep searchStep;
    @Mock
    private BuyClosedPackageItemStep buyStep;

    @Test
    public void testGetItemSearch() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Search(Product.CLOSED_PACKAGES, "BUE");
        ClosedPackagesItem itemHome = new ClosedPackagesItem("CLUID1");
        Mockito.when(this.searchStep.execute("BUE", action)).thenReturn(itemHome);
        ItemHome item = this.steps.execute("BUE", action);
        Mockito.verifyNoMoreInteractions(this.buyStep);
        Assert.assertNotNull(item);
        Assert.assertTrue(item.getId().equals("CLUID1"));
    }

    @Test
    public void testGetItemBuy() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.CLOSED_PACKAGES, "BUE");
        ClosedPackagesItem itemHome = new ClosedPackagesItem("CLUID1");
        Mockito.when(this.buyStep.execute("BUE", action)).thenReturn(itemHome);
        ItemHome item = this.steps.execute("BUE", action);
        Mockito.verifyNoMoreInteractions(this.searchStep);
        Assert.assertNotNull(item);
        Assert.assertTrue(item.getId().equals("CLUID1"));
    }
}
