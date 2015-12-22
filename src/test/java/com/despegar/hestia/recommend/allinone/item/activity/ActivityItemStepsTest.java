package com.despegar.hestia.recommend.allinone.item.activity;

import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ar.com.despegar.p13n.hestia.TestFuntions;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.ActivityItem;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.item.activity.ActivityItemSteps;
import com.despegar.p13n.hestia.recommend.allinone.item.activity.BuyActivitySteps;
import com.despegar.p13n.hestia.recommend.allinone.item.activity.SearchActivitySteps;

@RunWith(MockitoJUnitRunner.class)
public class ActivityItemStepsTest {

    private ActivityItemSteps steps;
    @Mock
    private SearchActivitySteps searchActivitySteps;
    @Mock
    private BuyActivitySteps buyActivitySteps;

    @Before
    public void setUp() {
        this.steps = new ActivityItemSteps(this.searchActivitySteps, this.buyActivitySteps);
    }

    @Test
    public void testGetItemBuy() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.ACTIVITIES, "MIA");
        action.setCountryCode(CountryCode.BR);
        ActivityItem item = new ActivityItem("ACT_ID");
        Mockito.when(this.buyActivitySteps.execute("MIA", action)).thenReturn(item);
        ActivityItem obtaindedItem = (ActivityItem) this.steps.execute("MIA", action);
        Mockito.verify(this.buyActivitySteps).execute("MIA", action);
        Mockito.verifyNoMoreInteractions(this.searchActivitySteps);
        Assert.assertNotNull(obtaindedItem);
        Assert.assertEquals(item, obtaindedItem);
    }

    @Test
    public void testGetItemSearch() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Search(Product.ACTIVITIES, "MIA");
        action.setCountryCode(CountryCode.BR);
        ActivityItem item = new ActivityItem("ACT_ID");
        Mockito.when(this.searchActivitySteps.execute("MIA", action)).thenReturn(item);
        ActivityItem obtaindedItem = (ActivityItem) this.steps.execute("MIA", action);
        Mockito.verify(this.searchActivitySteps).execute("MIA", action);
        Mockito.verifyNoMoreInteractions(this.buyActivitySteps);
        Assert.assertNotNull(obtaindedItem);
        Assert.assertEquals(item, obtaindedItem);
    }

}
