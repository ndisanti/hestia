package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import java.util.Arrays;
import java.util.List;

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
import com.despegar.p13n.hestia.api.data.model.ActivityItem;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RankingPositionDTO;
import com.despegar.p13n.hestia.api.data.model.RankingTreeDTO;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.client.RankingsClient;
import com.despegar.p13n.hestia.data.hbase.hot.types.StaticRankingTypes;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
@RunWith(MockitoJUnitRunner.class)
public class ActivitiesUniversalFunctionTest {

	@InjectMocks
    private ActivitiesUniversalFunction function = new ActivitiesUniversalFunction();
    @Mock
    private RankingsClient rankingsClient;
    
    @Before
    public void before() {

        List<RankingPositionDTO> PODIUM = Arrays.asList( //
            new RankingPositionDTO("HB_ORL_SEAWRLDROW", 40L), //
            new RankingPositionDTO("HB_ORL_LEGOFLORID", 30L), //
            new RankingPositionDTO("HB_ORL_BUSCHROW", 20L), //
            new RankingPositionDTO("HB_ORL_EATNPLAY", 10L),//
            new RankingPositionDTO("HB_ORL_EATNPLAY1", 10L),//
            new RankingPositionDTO("HB_ORL_EATNPLAY2", 10L));

        RankingTreeDTO ranking = new RankingTreeDTO();
        ranking.setPodium(PODIUM);
		Mockito.when(this.rankingsClient.getRankingFromIp(Mockito.any(Product.class), Mockito.anyString(),Mockito.any(StaticRankingTypes.class), Mockito.anyInt())).thenReturn(ranking );
    }

    @Test
    public void testSearchStar() {

        final ActionRecommendation context = TestFuntions.buildActionRecommendation4Search(Product.ACTIVITIES, "MIA");

        Offer offer = this.function.buildOffers(Product.ACTIVITIES, Product.ACTIVITIES, context, new Param()).get(0);

        Assert.assertNotNull(offer);
        Assert.assertNotNull(offer.getOffer());
        Assert.assertTrue(offer.getOffer() instanceof ActivityItem);
        Assert.assertEquals(ActivitiesDisneyFunction.UNIVERSAL, ((ActivityItem) offer.getOffer()).getActid());
    }

    @Test
    public void testSearchRow() {

        final ActionRecommendation context = TestFuntions.buildActionRecommendation4Search(Product.ACTIVITIES, "MIA");

        Param param = Param.builder().flow1(Flow.SEARCH).build();

        RowHome row = this.function.buildRow(Product.ACTIVITIES, Product.ACTIVITIES, context, param);
        Assert.assertNotNull(row);
        Assert.assertNotNull(row.getOffers());
        Assert.assertTrue(row.getOffers().get(0) instanceof ActivityItem);
        TestFuntions.assertActivityItem(row.getOffers().get(0), ActivitiesDisneyFunction.UNIVERSAL);
        TestFuntions.assertActivityItem(row.getOffers().get(1), "HB_ORL_SEAWRLDROW");
        TestFuntions.assertActivityItem(row.getOffers().get(2), "HB_ORL_LEGOFLORID");
        TestFuntions.assertActivityItem(row.getOffers().get(3), "HB_ORL_BUSCHROW");
    }

    @Test
    public void testNoHistoryRow() {

        final ActionRecommendation context = TestFuntions.buildActionRecommendation4Search(Product.ACTIVITIES, "MIA");

        Param param = Param.builder().flow1(Flow.SEARCH).build();

        RowHome row = this.function.buildRow(Product.ACTIVITIES, Product.ACTIVITIES, context, param);
        Assert.assertNotNull(row);
        Assert.assertNotNull(row.getOffers());
        Assert.assertTrue(row.getOffers().get(0) instanceof ActivityItem);
        TestFuntions.assertActivityItem(row.getOffers().get(0), ActivitiesUniversalFunction.UNIVERSAL);
        TestFuntions.assertActivityItem(row.getOffers().get(1), "HB_ORL_SEAWRLDROW");
        TestFuntions.assertActivityItem(row.getOffers().get(2), "HB_ORL_LEGOFLORID");
        TestFuntions.assertActivityItem(row.getOffers().get(3), "HB_ORL_BUSCHROW");
    }

}
