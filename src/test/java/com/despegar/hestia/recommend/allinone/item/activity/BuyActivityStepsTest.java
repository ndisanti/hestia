package com.despegar.hestia.recommend.allinone.item.activity;

import java.util.List;

import org.junit.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ar.com.despegar.p13n.hestia.TestFuntions;

import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.recommendations.Recommendation;
import com.despegar.p13n.hestia.api.data.model.ActivityItem;
import com.despegar.p13n.hestia.client.RankingsClient;
import com.despegar.p13n.hestia.client.RecommendationsClient;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.item.activity.ActivityStepsCommonsFunctions;
import com.despegar.p13n.hestia.recommend.allinone.item.activity.BuyActivitySteps;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.BaseFunction;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class BuyActivityStepsTest {

	@InjectMocks
    private BuyActivitySteps steps = new BuyActivitySteps();
    @Mock
    private ActivityStepsCommonsFunctions commons;
    @Mock
    private RankingsClient hotRankingService;
    @Mock
    private RecommendationsClient recommendation;

    @Test
    public void testGetItemBuyActivity() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.ACTIVITIES, "MIA");
        List<Recommendation> recommendations = Lists.newArrayList();
        recommendations.add(new Recommendation("ACT_ID", 33));
        Mockito.when(
            this.recommendation.recommend(Mockito.eq(Product.ACTIVITIES), Mockito.eq(Flow.DETAIL),
                Mockito.eq(Product.ACTIVITIES), Mockito.eq(Flow.DETAIL), Mockito.anyString(),
                Mockito.eq(BaseFunction.RANKING_SIZE))).thenReturn(recommendations);
        ActivityItem obtainedItem = (ActivityItem) this.steps.execute("MIA", action);
        Assert.assertNotNull(obtainedItem);
        Assert.assertTrue(obtainedItem.getActid().equals("ACT_ID"));
        ActivityItem item = new ActivityItem("ACT_ID2");
        Mockito.when(this.commons.getItemFromRanking(action, "MIA")).thenReturn(item);
        obtainedItem = (ActivityItem) this.steps.execute("MIA", action);
        Assert.assertNotNull(obtainedItem);
        Assert.assertTrue(obtainedItem.getActid().equals("ACT_ID2"));
    }

    @Test
    public void testGetItemBuy() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.CARS, "MIA");
        ActivityItem item = new ActivityItem("ACT_ID");
        Mockito.when(this.commons.getItemFromRanking(action, "MIA")).thenReturn(item, null);
        ActivityItem obtainedItem = (ActivityItem) this.steps.execute("MIA", action);
        Assert.assertNotNull(obtainedItem);
        Assert.assertTrue(obtainedItem.getActid().equals("ACT_ID"));
        obtainedItem = (ActivityItem) this.steps.execute("MIA", action);
        Assert.assertNull(obtainedItem);
    }


}
