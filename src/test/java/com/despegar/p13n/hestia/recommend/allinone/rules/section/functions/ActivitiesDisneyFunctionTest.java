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

import ar.com.despegar.p13n.hestia.TestFuntions;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.UserContext;
import com.despegar.p13n.euler.commons.client.model.UserLocation;
import com.despegar.p13n.euler.commons.client.model.UserRecord;
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
public class ActivitiesDisneyFunctionTest {

	@InjectMocks
    private ActivitiesDisneyFunction function = new ActivitiesDisneyFunction();
  
	@Mock
	private RankingsClient rankingService;
	@Mock
    private UserContext userContextAccesor;
    @Mock
    private UserRecord userPrifileRecord;
    @Mock
    private UserLocation homeUserLocation;

    List<RankingPositionDTO> PODIUM = Arrays.asList( //
            new RankingPositionDTO("HB_ORL_SEAWRLDROW", 40L), //
            new RankingPositionDTO("HB_ORL_LEGOFLORID", 30L), //
            new RankingPositionDTO("HB_ORL_BUSCHROW", 20L), //
            new RankingPositionDTO("HB_ORL_BUSCHROW1", 20L), //
            new RankingPositionDTO("HB_ORL_BUSCHROW2", 20L), //
            new RankingPositionDTO("HB_ORL_EATNPLAY", 10L));
    
    @Before
    public void before() {

        Mockito.when(this.homeUserLocation.getIp()).thenReturn(null);
        Mockito.when(this.userContextAccesor.getUserRecord()).thenReturn(this.userPrifileRecord);
        Mockito.when(this.userContextAccesor.getUserLocation()).thenReturn(this.homeUserLocation);
        Mockito.when(this.userPrifileRecord.getLastIntenseSearch()).thenReturn(null);
        RankingTreeDTO ranking = new RankingTreeDTO();
        ranking.setPodium(PODIUM);
		Mockito.when(this.rankingService.getRankingFromIp(Mockito.any(Product.class), Mockito.anyString(), Mockito.any(StaticRankingTypes.class), Mockito.anyInt())).thenReturn(ranking );
		Mockito.when(this.rankingService.getRanking(Mockito.any(Product.class), Mockito.any(StaticRankingTypes.class), Mockito.any(CountryCode.class), Mockito.anyInt())).thenReturn(ranking);
    }


    @Test
    public void testBuyRow() {
        final ActionRecommendation context = TestFuntions.buildActionRecommendation4Buy(Product.ACTIVITIES, "MIA");
        context.setUserContext(this.userContextAccesor);
        Param param = Param.builder().flow1(Flow.THANKS).build();

        RowHome row = this.function.buildRow(Product.ACTIVITIES, Product.ACTIVITIES, context, param);
        Assert.assertNotNull(row);
        Assert.assertNotNull(row.getOffers());
        Assert.assertTrue(row.getOffers().get(0) instanceof ActivityItem);
        TestFuntions.assertActivityItem(row.getOffers().get(0), ActivitiesDisneyFunction.DISNEY);
        TestFuntions.assertActivityItem(row.getOffers().get(1), ActivitiesDisneyFunction.UNIVERSAL);
        TestFuntions.assertActivityItem(row.getOffers().get(2), "HB_ORL_SEAWRLDROW");
        TestFuntions.assertActivityItem(row.getOffers().get(3), "HB_ORL_LEGOFLORID");
    }

    @Test
    public void testSearchStarAndRow() {

        final ActionRecommendation context = TestFuntions.buildActionRecommendation4Search(Product.ACTIVITIES, "MIA");

        Offer offer = this.function.buildOffers(Product.ACTIVITIES, Product.ACTIVITIES, context, new Param()).get(0);

        Assert.assertNotNull(offer);
        Assert.assertNotNull(offer.getOffer());
        Assert.assertTrue(offer.getOffer() instanceof ActivityItem);
        Assert.assertEquals(ActivitiesDisneyFunction.DISNEY, ((ActivityItem) offer.getOffer()).getActid());

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
    public void testSearchRowOnly() {

        final ActionRecommendation context = TestFuntions.buildActionRecommendation4Search(Product.ACTIVITIES, "MIA");

        Param param = Param.builder().flow1(Flow.SEARCH).build();

        RowHome row = this.function.buildRow(Product.ACTIVITIES, Product.ACTIVITIES, context, param);
        Assert.assertNotNull(row);
        Assert.assertNotNull(row.getOffers());
        Assert.assertTrue(row.getOffers().get(0) instanceof ActivityItem);
        TestFuntions.assertActivityItem(row.getOffers().get(0), ActivitiesDisneyFunction.DISNEY);
        TestFuntions.assertActivityItem(row.getOffers().get(1), ActivitiesDisneyFunction.UNIVERSAL);
        TestFuntions.assertActivityItem(row.getOffers().get(2), "HB_ORL_SEAWRLDROW");
        TestFuntions.assertActivityItem(row.getOffers().get(3), "HB_ORL_LEGOFLORID");
    }

    @Test
    public void testNoHistoryRow() {

        final ActionRecommendation context = TestFuntions.buildActionRecommendation4Search(Product.ACTIVITIES, "MIA");

        Param param = Param.builder().flow1(Flow.SEARCH).build();

        RowHome row = this.function.buildRow(Product.ACTIVITIES, Product.ACTIVITIES, context, param);
        Assert.assertNotNull(row);
        Assert.assertNotNull(row.getOffers());
        Assert.assertTrue(row.getOffers().get(0) instanceof ActivityItem);
        TestFuntions.assertActivityItem(row.getOffers().get(0), ActivitiesDisneyFunction.DISNEY);
        TestFuntions.assertActivityItem(row.getOffers().get(1), ActivitiesDisneyFunction.UNIVERSAL);
        TestFuntions.assertActivityItem(row.getOffers().get(2), "HB_ORL_SEAWRLDROW");
        TestFuntions.assertActivityItem(row.getOffers().get(3), "HB_ORL_LEGOFLORID");
    }

}
