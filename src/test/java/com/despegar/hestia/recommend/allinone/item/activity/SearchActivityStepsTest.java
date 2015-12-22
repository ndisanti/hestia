package com.despegar.hestia.recommend.allinone.item.activity;

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
import com.despegar.p13n.hestia.api.data.model.ActivityItem;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.SearchActivity;
import com.despegar.p13n.hestia.recommend.allinone.activity.UserActivity;
import com.despegar.p13n.hestia.recommend.allinone.item.activity.ActivityStepsCommonsFunctions;
import com.despegar.p13n.hestia.recommend.allinone.item.activity.SearchActivitySteps;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

@RunWith(MockitoJUnitRunner.class)
public class SearchActivityStepsTest {

    private SearchActivitySteps step;
    @Mock
    private ActivityStepsCommonsFunctions commons;

    @Before
    public void setUp() {
        this.step = new SearchActivitySteps(this.commons);
    }

    @Test
    public void testGetItemOrlMia() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Search(Product.ACTIVITIES, "MIA");
        ItemHome item = this.step.execute("ORL", action);
        Assert.assertNotNull(item);
        Assert.assertTrue(item.getId().equals("DY_ORL"));
        item = this.step.execute("MIA", action);
        Assert.assertNotNull(item);
        Assert.assertTrue(item.getId().equals("UN_ORL"));
        SearchActivity searchActivity = Mockito.mock(SearchActivity.class);
        action.setSearchActivity(searchActivity);
        Multimap<String, String> actBydestination = LinkedHashMultimap.create();
        actBydestination.put("MIA", "ACT_ID1");
        Mockito.when(searchActivity.getLastActivitiesByDestination()).thenReturn(actBydestination);
        item = this.step.execute("MIA", action);
        Assert.assertNotNull(item);
        Assert.assertTrue(item.getId().equals("ACT_ID1"));
        ActivityItem activityItem = new ActivityItem("ACT_ID2");
        Mockito.when(this.commons.getItemFromRanking(action, "MIA")).thenReturn(activityItem);
        item = this.step.execute("MIA", action);
        Mockito.verify(this.commons).getItemFromRanking(action, "MIA");
        Assert.assertNotNull(item);
        Assert.assertTrue(item.getId().equals("ACT_ID2"));
    }

    @Test
    public void testGetItemDefault() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Search(Product.ACTIVITIES, "MIA");
        SearchActivity searchActivity = Mockito.mock(SearchActivity.class);
        action.setSearchActivity(searchActivity);
        UserActivity userActivity = Mockito.mock(UserActivity.class);
        UserAction userAction = new UserAction();
        userAction.setFlow(Flow.DETAIL);
        Mockito.when(userActivity.getAction()).thenReturn(userAction);
        Mockito.when(userActivity.getDestination()).thenReturn("BUE");
        Mockito.when(searchActivity.getActivity(Product.ACTIVITIES)).thenReturn(userActivity);
        Multimap<String, String> actBydestination = LinkedHashMultimap.create();
        actBydestination.put("RIO", "ACT_ID1");
        Mockito.when(searchActivity.getLastActivitiesByDestination()).thenReturn(actBydestination);
        ItemHome item = this.step.execute("RIO", action);
        Assert.assertNotNull(item);
        Assert.assertTrue(item.getId().equals("ACT_ID1"));
        ActivityItem activityItem = new ActivityItem("ACT_ID2");
        Mockito.when(this.commons.getItemFromRanking(action, "RIO")).thenReturn(activityItem);
        item = this.step.execute("RIO", action);
        Assert.assertNotNull(item);
        Assert.assertTrue(item.getId().equals("ACT_ID2"));
        userAction.setFlow(Flow.CHECKOUT);
        item = this.step.execute("RIO", action);
        Assert.assertNotNull(item);
        Assert.assertTrue(item.getId().equals("RIO"));
        activityItem.setActid("ACT_ID3");
        item = this.step.execute("RIO", action);
        Assert.assertNotNull(item);
        Assert.assertTrue(item.getId().equals("ACT_ID3"));
    }
}
