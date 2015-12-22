/**
 * 
 */
package com.despegar.p13n.hestia.recommend.allinone.rules.section.functions;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ar.com.despegar.p13n.hestia.test.MockitoAnnotationBaseTest;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RankingTreeDTO;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.client.RankingsClient;
import com.despegar.p13n.hestia.data.hbase.hot.types.StaticRankingTypes;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class HotRankingIpCountryFunctionTest
    extends MockitoAnnotationBaseTest {

	@InjectMocks
	private HotRankingIpCountryFunction function = new HotRankingIpCountryFunction();
	@Mock
	private RankingsClient rankingsClient;
	
    @Test
    public void testNoDataNoIp() {
        RankingTreeDTO ranking = new RankingTreeDTO();
        ranking.setCity("c");

		Mockito.when(this.rankingsClient.getRankingFromIp(Mockito.any(Product.class), Mockito.anyString(),Mockito.any(StaticRankingTypes.class), Mockito.anyInt())).thenReturn(ranking );        final ActionRecommendation context = TestFuntions.buildActionRecommendation4Buy(Product.CRUISES, "MIA");
        context.setOrigin("BUE");

        Param param = Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_ANY).build();

        List<Offer> offers = function.buildOffers(Product.HOTELS, Product.HOTELS, context, param);

        Assert.assertNull(offers);

        RowHome buildRow = function.buildRow(Product.HOTELS, Product.HOTELS, context, param);

        Assert.assertNull(buildRow);
    }

    @Test
    public void testNoDataIp() {
        RankingTreeDTO ranking = new RankingTreeDTO();
        ranking.setCity("c");
		Mockito.when(this.rankingsClient.getRankingFromIp(Mockito.any(Product.class), Mockito.anyString(),Mockito.any(StaticRankingTypes.class), Mockito.anyInt())).thenReturn(ranking );
        final ActionRecommendation context = TestFuntions.buildActionRecommendation4Buy(Product.CRUISES, "MIA");
        context.setOrigin("BUE");

        Param param = Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_ANY).build();

        List<Offer> offers = function.buildOffers(Product.HOTELS, Product.HOTELS, context, param);

        Assert.assertNull(offers);

        RowHome buildRow = function.buildRow(Product.HOTELS, Product.HOTELS, context, param);

        Assert.assertNull(buildRow);
    }

    @Test
    public void testSomeDataNoIp() {
        RankingTreeDTO ranking = new RankingTreeDTO();
        ranking.setCity("c");
        ranking.addPosition("c1", 1l);
        ranking.addPosition("c2", 2l);
        ranking.addPosition("c3", 3l);
        ranking.addPosition("c4", 4l);
        ranking.addPosition("c5", 5l);
        ranking.addPosition("c6", 5l);
        ranking.addPosition("c7", 5l);
        ranking.addPosition("c8", 5l);

        RankingTreeDTO rankingChild = new RankingTreeDTO();
        rankingChild.setCity("d");
        rankingChild.addPosition("d1", 1l);
        rankingChild.addPosition("d2", 2l);
        rankingChild.addPosition("d3", 3l);
        rankingChild.addPosition("d4", 4l);
        rankingChild.addPosition("d5", 5l);
        rankingChild.addPosition("d6", 5l);
        rankingChild.addPosition("d7", 5l);
        rankingChild.addPosition("d8", 5l);


        ranking.setChild(rankingChild);
		Mockito.when(this.rankingsClient.getRankingFromIp(Mockito.any(Product.class), Mockito.anyString(),Mockito.any(StaticRankingTypes.class), Mockito.anyInt())).thenReturn(ranking );
        final ActionRecommendation context = TestFuntions.buildActionRecommendation4Buy(Product.CRUISES, "MIA");
        context.setOrigin("BUE");

        Param param = Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_ANY).build();

        Offer offer = function.buildOffers(Product.HOTELS, Product.HOTELS, context, param).get(0);

        TestFuntions.assertHotelDestination(offer.getOffer(), "c1");

        RowHome row = function.buildRow(Product.HOTELS, Product.HOTELS, context, param);

        Assert.assertNull(row.getHighlighted());
        Assert.assertEquals(7, row.getOffers().size());
        TestFuntions.assertHotelDestination(row.getOffers().get(0), "c2");
        TestFuntions.assertHotelDestination(row.getOffers().get(1), "c3");
    }

    @Test
    public void testSomeDataIp() {
        RankingTreeDTO ranking = new RankingTreeDTO();
        ranking.setCity("c");
        ranking.addPosition("c1", 1l);
        ranking.addPosition("c2", 2l);
        ranking.addPosition("c3", 3l);
        ranking.addPosition("c4", 4l);
        ranking.addPosition("c5", 5l);
        ranking.addPosition("c6", 5l);
        ranking.addPosition("c7", 5l);
        ranking.addPosition("c8", 5l);

        RankingTreeDTO rankingChild = new RankingTreeDTO();
        rankingChild.setCity("d");
        rankingChild.addPosition("d1", 1l);
        rankingChild.addPosition("d2", 2l);
        rankingChild.addPosition("d3", 3l);
        rankingChild.addPosition("d4", 4l);
        rankingChild.addPosition("d5", 5l);
        rankingChild.addPosition("d6", 6l);
        rankingChild.addPosition("d7", 5l);
        rankingChild.addPosition("d8", 6l);

        ranking.setChild(rankingChild);
		Mockito.when(this.rankingsClient.getRankingFromIp(Mockito.any(Product.class), Mockito.anyString(),Mockito.any(StaticRankingTypes.class), Mockito.anyInt())).thenReturn(ranking );
        final ActionRecommendation context = TestFuntions.buildActionRecommendation4Buy(Product.CRUISES, "MIA");
        context.setOrigin("BUE");

        Param param = Param.builder().rankingType(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_ANY).build();

        Offer offer = function.buildOffers(Product.HOTELS, Product.HOTELS, context, param).get(0);

        TestFuntions.assertHotelDestination(offer.getOffer(), "c1");

        RowHome row = function.buildRow(Product.HOTELS, Product.HOTELS, context, param);

        Assert.assertNull(row.getHighlighted());
        Assert.assertEquals(7, row.getOffers().size());
        TestFuntions.assertHotelDestination(row.getOffers().get(0), "c2");
        TestFuntions.assertHotelDestination(row.getOffers().get(1), "c3");
    }


}
