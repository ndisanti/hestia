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
public class ActivitiesRankingLocationFunctionTest {

	@InjectMocks
    private ActivitiesRankingLocationFunction function = new ActivitiesRankingLocationFunction();
@Mock
private RankingsClient rankingsClient;
	
    @Mock
    private UserContext userContextAccesor;
    @Mock
    private UserRecord userPrifileRecord;
    @Mock
    private UserLocation homeUserLocation;


    @Before
    public void before() {

        List<RankingPositionDTO> PODIUM = Arrays.asList( //
            new RankingPositionDTO("DY_ORL", 50L), //
            new RankingPositionDTO("HB_ORL_SEAWRLDROW", 40L), //
            new RankingPositionDTO("HB_ORL_LEGO", 30L), //
            new RankingPositionDTO("HB_ORL_WET", 20L), //
            new RankingPositionDTO("HB_ORL_BUSCH", 10L), //
            new RankingPositionDTO("HB_ORL_111", 10L), //
            new RankingPositionDTO("HB_ORL_112", 10L), //
            new RankingPositionDTO("HB_ORL_113", 10L));
RankingTreeDTO ranking = new RankingTreeDTO();
ranking.setPodium(PODIUM);
Mockito.when(this.rankingsClient.getRanking(Mockito.any(Product.class), Mockito.any(StaticRankingTypes.class),Mockito.any(CountryCode.class), Mockito.anyInt())).thenReturn(ranking );
        Mockito.when(this.userContextAccesor.getUserRecord()).thenReturn(this.userPrifileRecord);
        Mockito.when(this.userContextAccesor.getUserLocation()).thenReturn(this.homeUserLocation);

    }

    @Test
    public void testBuyStarAndRow() {

        final ActionRecommendation context = TestFuntions.buildActionRecommendation4Buy(Product.ACTIVITIES, "BUE");
        context.setUserContext(this.userContextAccesor);
        Offer offer = this.function.buildOffers(Product.ACTIVITIES, Product.ACTIVITIES, context, new Param()).get(0);

        Assert.assertNotNull(offer);
        Assert.assertNotNull(offer.getOffer());
        Assert.assertTrue(offer.getOffer() instanceof ActivityItem);
        TestFuntions.assertActivityItem(offer.getOffer(), "DY_ORL");

        Param param = Param.builder().flow1(Flow.SEARCH).build();

        RowHome row = this.function.buildRow(Product.ACTIVITIES, Product.ACTIVITIES, context, param);
        Assert.assertNotNull(row);
        Assert.assertNotNull(row.getOffers());
        Assert.assertTrue(row.getOffers().get(0) instanceof ActivityItem);
        TestFuntions.assertActivityItem(row.getOffers().get(0), "HB_ORL_SEAWRLDROW");
        TestFuntions.assertActivityItem(row.getOffers().get(1), "HB_ORL_LEGO");
        TestFuntions.assertActivityItem(row.getOffers().get(2), "HB_ORL_WET");
        TestFuntions.assertActivityItem(row.getOffers().get(3), "HB_ORL_BUSCH");
        TestFuntions.assertActivityItem(row.getOffers().get(4), "HB_ORL_111");
        TestFuntions.assertActivityItem(row.getOffers().get(5), "HB_ORL_112");
        TestFuntions.assertActivityItem(row.getOffers().get(6), "HB_ORL_113");
    }

    @Test
    public void testSearchRow() {

        final ActionRecommendation context = TestFuntions.buildActionRecommendation4Buy(Product.ACTIVITIES, "BUE");
        context.setUserContext(this.userContextAccesor);
        Param param = Param.builder().flow1(Flow.SEARCH).build();

        RowHome row = this.function.buildRow(Product.ACTIVITIES, Product.ACTIVITIES, context, param);
        Assert.assertNotNull(row);
        Assert.assertNotNull(row.getOffers());
        Assert.assertTrue(row.getOffers().get(0) instanceof ActivityItem);
        TestFuntions.assertActivityItem(row.getOffers().get(0), "DY_ORL");
        TestFuntions.assertActivityItem(row.getOffers().get(1), "HB_ORL_SEAWRLDROW");
        TestFuntions.assertActivityItem(row.getOffers().get(2), "HB_ORL_LEGO");
        TestFuntions.assertActivityItem(row.getOffers().get(3), "HB_ORL_WET");
    }

}
