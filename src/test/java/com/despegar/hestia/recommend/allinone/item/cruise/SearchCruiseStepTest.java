package com.despegar.hestia.recommend.allinone.item.cruise;

import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ar.com.despegar.p13n.hestia.TestFuntions;

import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.UserAction;
import com.despegar.p13n.hestia.api.data.model.CruiseItem;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.SearchActivity;
import com.despegar.p13n.hestia.recommend.allinone.activity.UserActivity;
import com.despegar.p13n.hestia.recommend.allinone.item.cruise.CruiseCommonsFunctions;
import com.despegar.p13n.hestia.recommend.allinone.item.cruise.SearchCruiseStep;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

@RunWith(MockitoJUnitRunner.class)
public class SearchCruiseStepTest {

    private SearchCruiseStep steps;
    @Mock
    private CruiseCommonsFunctions commons;

    @Before
    public void setUp() {
        this.steps = new SearchCruiseStep(this.commons);
    }

    @Test
    public void testGetItem() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Search(Product.CRUISES, "BUE");
        SearchActivity searchActivity = Mockito.mock(SearchActivity.class);
        action.setSearchActivity(searchActivity);
        UserActivity userActivity = Mockito.mock(UserActivity.class);
        UserAction userAction = new UserAction();
        userAction.setFlow(Flow.DETAIL);
        Mockito.when(userActivity.getAction()).thenReturn(userAction);
        Multimap<String, String> cruisesBydestination = LinkedHashMultimap.create();
        cruisesBydestination.put("BUE", "DID1");
        Mockito.when(searchActivity.getLastCruisesByDestination()).thenReturn(cruisesBydestination);
        Mockito.when(searchActivity.getActivity(Product.CRUISES)).thenReturn(userActivity);
        ItemHome item = this.steps.execute("BUE", action);
        Assert.assertNotNull(item);
        Assert.assertTrue(item.getId().equals("DID1"));
        item = this.steps.execute("BUE", action);
        Assert.assertNull(item);
        CruiseItem itemHome = new CruiseItem("DID2");
        Mockito.when(this.commons.getItemFromRanking("BUE", action)).thenReturn(itemHome);
        userAction.setFlow(Flow.THANKS);
        item = this.steps.execute("BUE", action);
        Assert.assertNotNull(item);
        Assert.assertTrue(item.getId().equals("DID2"));

    }

}
