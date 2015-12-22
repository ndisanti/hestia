package com.despegar.hestia.recommend.allinone.item.cruise;

import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ar.com.despegar.p13n.hestia.TestFuntions;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.CruiseItem;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.item.cruise.BuyCruiseStep;
import com.despegar.p13n.hestia.recommend.allinone.item.cruise.CruiseItemSteps;
import com.despegar.p13n.hestia.recommend.allinone.item.cruise.SearchCruiseStep;

@RunWith(MockitoJUnitRunner.class)
public class CruiseItemStepsTest {

    private CruiseItemSteps steps;
    @Mock
    private SearchCruiseStep searchCruiseStep;
    @Mock
    private BuyCruiseStep buyCruiseStep;

    @Before
    public void setUp() {
        this.steps = new CruiseItemSteps(this.searchCruiseStep, this.buyCruiseStep);
    }

    @Test
    public void testGetItemSearch() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Search(Product.CRUISES, "BUE");
        ItemHome cruiseItem = new CruiseItem("DID1");
        Mockito.when(this.searchCruiseStep.execute("MIA", action)).thenReturn(cruiseItem);
        CruiseItem item = (CruiseItem) this.steps.execute("MIA", action);
        Mockito.verifyNoMoreInteractions(this.buyCruiseStep);
        Assert.assertNotNull(item);
        Assert.assertTrue(item.getDid().equals("DID1"));
    }

    @Test
    public void testGetItemBuy() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.CRUISES, "BUE");
        ItemHome cruiseItem = new CruiseItem("DID1");
        Mockito.when(this.buyCruiseStep.execute("MIA", action)).thenReturn(cruiseItem);
        CruiseItem item = (CruiseItem) this.steps.execute("MIA", action);
        Mockito.verifyNoMoreInteractions(this.searchCruiseStep);
        Assert.assertNotNull(item);
        Assert.assertTrue(item.getDid().equals("DID1"));
    }

}
