package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.recommendations.Recommendation;
import com.despegar.p13n.hestia.api.data.model.ActivityItem;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.client.RecommendationsClient;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;

@RunWith(MockitoJUnitRunner.class)
public class ActivitiesViewedFunctionTest {

	@InjectMocks
    private ActivitiesViewedFunction function = new ActivitiesViewedFunction();
@Mock
private RecommendationsClient rs;
	
    @Before
    public void before() {

   Mockito.when(this.rs.recommend(Mockito.any(Product.class), Mockito.any(Flow.class), Mockito.any(Product.class), Mockito.any(Flow.class),Mockito.anyString(), Mockito.anyInt())).thenReturn(Arrays.asList(//
           new Recommendation("HB_ORL_SEAWRLDROW", 9), //
           new Recommendation("HB_ORL_LEGOFLORID", 8), //
           new Recommendation("HB_ORL_BUSCHROW", 7), //
           new Recommendation("HB_ORL_EATNPLAY", 6), new Recommendation("HB_ORL_EATNPLAY1", 6), new Recommendation(
               "HB_ORL_EATNPLAY2", 6), new Recommendation("HB_ORL_EATNPLAY3", 6)));
    }

    @Test
    public void testSearchStar() {
        final ActionRecommendation context = TestFuntions.buildActionRecommendation4DetailAndViewed(Product.ACTIVITIES,
            "MIA", "DY_ORL");

        Offer offer = this.function.buildOffers(Product.ACTIVITIES, Product.ACTIVITIES, context, new Param()).get(0);
        Assert.assertNotNull(offer);
        Assert.assertNotNull(offer.getOffer());
        Assert.assertTrue(offer.getOffer() instanceof ActivityItem);
        TestFuntions.assertActivityItem(offer.getOffer(), ActivitiesDisneyFunction.DISNEY);
    }

    @Test
    public void testSearchRow() {
        final ActionRecommendation context = TestFuntions.buildActionRecommendation4DetailAndViewed(Product.ACTIVITIES,
            "MIA", "DY_ORL");

        RowHome row = this.function.buildRow(Product.ACTIVITIES, Product.ACTIVITIES, context, new Param());
        Assert.assertNotNull(row);
        Assert.assertNotNull(row.getOffers());
        Assert.assertNotNull(row.getOffers().get(0));
        Assert.assertTrue(row.getOffers().get(0) instanceof ActivityItem);
        TestFuntions.assertActivityItem(row.getOffers().get(0), ActivitiesDisneyFunction.DISNEY);
        TestFuntions.assertActivityItem(row.getOffers().get(1), "HB_ORL_SEAWRLDROW");
        TestFuntions.assertActivityItem(row.getOffers().get(2), "HB_ORL_LEGOFLORID");
        TestFuntions.assertActivityItem(row.getOffers().get(3), "HB_ORL_BUSCHROW");
    }

}
