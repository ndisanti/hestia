package com.despegar.hestia.recommend.allinone.item.cruise;

import java.util.ArrayList;
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
import com.despegar.p13n.hestia.api.data.model.CruiseItem;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.client.RankingsClient;
import com.despegar.p13n.hestia.client.RecommendationsClient;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.item.cruise.BuyCruiseStep;
import com.despegar.p13n.hestia.recommend.allinone.item.cruise.CruiseCommonsFunctions;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.BaseFunction;

@RunWith(MockitoJUnitRunner.class)
public class BuyCruiseStepTest {


	@InjectMocks
    private BuyCruiseStep steps = new BuyCruiseStep();
    @Mock
    private CruiseCommonsFunctions commons;
    @Mock
    private RankingsClient hotRankingService;
    @Mock
    private RecommendationsClient recommendation;

     @Test
    public void testGetItemBuyCruises() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.CRUISES, "BUE");
        List<Recommendation> recommendations = new ArrayList<Recommendation>();
        recommendations.add(new Recommendation("DID1", 88));
        recommendations.add(new Recommendation("DID1", 44));
        Mockito.when(
            this.recommendation.recommend(Mockito.eq(Product.CRUISES), Mockito.eq(Flow.DETAIL),
                Mockito.eq(Product.CRUISES), Mockito.eq(Flow.DETAIL), Mockito.anyString(),
                Mockito.eq(BaseFunction.RANKING_SIZE))).thenReturn(recommendations);
        ItemHome item = this.steps.execute("BUE", action);
        Assert.assertNotNull(item);
        Assert.assertTrue(item.getId().equals("DID1"));
        CruiseItem cruiseItem = new CruiseItem("DID2");
        Mockito.when(this.commons.getItemFromRanking("BUE", action)).thenReturn(cruiseItem);
        item = this.steps.execute("BUE", action);
        Assert.assertNotNull(item);
        Assert.assertTrue(item.getId().equals("DID2"));
    }

    @Test
    public void testGetItemBuyDefault() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.CARS, "BUE");
        CruiseItem cruiseItem = new CruiseItem("DID2");
        Mockito.when(this.commons.getItemFromRanking("BUE", action)).thenReturn(cruiseItem);
        ItemHome item = this.steps.execute("BUE", action);
        Assert.assertNotNull(item);
        Assert.assertTrue(item.getId().equals("DID2"));
    }
}
