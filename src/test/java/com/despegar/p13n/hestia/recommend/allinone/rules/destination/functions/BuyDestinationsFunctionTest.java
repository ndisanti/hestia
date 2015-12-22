package com.despegar.p13n.hestia.recommend.allinone.rules.destination.functions;

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

import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.UserContext;
import com.despegar.p13n.euler.commons.client.model.UserLocation;
import com.despegar.p13n.euler.commons.client.model.UserRecord;
import com.despegar.p13n.euler.commons.client.model.recommendations.Recommendation;
import com.despegar.p13n.hestia.api.data.model.RankingPositionDTO;
import com.despegar.p13n.hestia.api.data.model.RankingTreeDTO;
import com.despegar.p13n.hestia.client.RankingsClient;
import com.despegar.p13n.hestia.client.RecommendationsClient;
import com.despegar.p13n.hestia.data.hbase.hot.types.StaticRankingTypes;
import com.despegar.p13n.hestia.external.geo.GeoService;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.BaseFunction;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class BuyDestinationsFunctionTest {

	@InjectMocks
    private BuyDestinationsFunction function = new BuyDestinationsFunction();
    
    private ActionRecommendation context;
    @Mock
    private RecommendationsClient rs;
    @Mock
    private GeoService geoService;
    @Mock
    private RankingsClient rankingService;
    @Mock
    private UserContext userContextAccesor;
    @Mock
    private UserRecord userPrifileRecord;
    @Mock
    private UserLocation homeUserLocation;

    @Before
    public void setUp() {

        Mockito.when(this.homeUserLocation.getIp()).thenReturn("127.0.0.1");
        Mockito.when(this.userContextAccesor.getUserLocation()).thenReturn(this.homeUserLocation);

        this.context = TestFuntions.buildActionRecommendation4Buy(Product.FLIGHTS, "MIA");
        this.context.setUserContext(this.userContextAccesor);
        this.context.setCurrentHome(Product.HOTELS);
        this.context.setIsCountryIpValid(true);
        Mockito.when(this.userContextAccesor.getUserRecord()).thenReturn(this.userPrifileRecord);
        Mockito.when(this.userPrifileRecord.getLastIntenseSearch()).thenReturn(null);
    }

    @Test
    public void testGetDestinations() {

        List<Recommendation> recList = Arrays.asList(new Recommendation("ORL", 10), new Recommendation("MIA", 9),
            new Recommendation("BUE", 8), new Recommendation("RIO", 7), new Recommendation("GRU", 6), new Recommendation(
                "LIM", 6), new Recommendation("NYC", 6), new Recommendation("CUN", 6), new Recommendation("SCL", 6),
            new Recommendation("AAA", 6), new Recommendation("BBB", 6), new Recommendation("CCC", 6));
        Mockito.when(
            this.rs.recommend(Mockito.eq(Product.HOTELS), Mockito.eq(Flow.THANKS), Mockito.eq(Product.HOTELS),
                Mockito.eq(Flow.THANKS), Mockito.anyString(), Mockito.eq(12))).thenReturn(recList);
        List<String> destinations = this.function.getItemIds(this.context);
        Assert.assertNotNull(destinations);
        Assert.assertTrue(destinations.size() == 12);
        Assert.assertTrue(destinations.get(0).equals("ORL"));
        Assert.assertTrue(destinations.get(1).equals("MIA"));
        Assert.assertTrue(destinations.get(2).equals("BUE"));
        Assert.assertTrue(destinations.get(3).equals("RIO"));
        Assert.assertTrue(destinations.get(4).equals("GRU"));
        Assert.assertTrue(destinations.get(5).equals("LIM"));
        Assert.assertTrue(destinations.get(6).equals("NYC"));
        Assert.assertTrue(destinations.get(7).equals("CUN"));
        Assert.assertTrue(destinations.get(8).equals("SCL"));
        Assert.assertTrue(destinations.get(9).equals("AAA"));
        Assert.assertTrue(destinations.get(10).equals("BBB"));
        Assert.assertTrue(destinations.get(11).equals("CCC"));

    }

    @Test
    public void testGetDestinations2() {

        List<Recommendation> recList = Arrays.asList(new Recommendation("ORL", 10), new Recommendation("MIA", 9));
        Mockito.when(
            this.rs.recommend(Mockito.eq(Product.HOTELS), Mockito.eq(Flow.THANKS), Mockito.eq(Product.HOTELS),
                Mockito.eq(Flow.THANKS), Mockito.anyString(), Mockito.eq(12))).thenReturn(recList);
        List<Recommendation> nearbycities = Arrays.asList(new Recommendation("BUE", 10), new Recommendation("RIO", 9),
            new Recommendation("GRU", 10), new Recommendation("LIM", 9), new Recommendation("NYC", 10), new Recommendation(
                "CUN", 9), new Recommendation("SCL", 9), new Recommendation("AAA", 9), new Recommendation("BBB", 9),
            new Recommendation("CCC", 9));
        Mockito.when(
            this.rs.recommend(Mockito.eq(Product.HOTELS), Mockito.eq(Flow.SEARCH), Mockito.eq(Product.HOTELS),
                Mockito.eq(Flow.SEARCH), Mockito.anyString(), Mockito.eq(10))).thenReturn(nearbycities);
        List<String> destinations = this.function.getItemIds(this.context);
        Assert.assertNotNull(destinations);
        Assert.assertTrue(destinations.size() == 12);
        Assert.assertTrue(destinations.get(0).equals("ORL"));
        Assert.assertTrue(destinations.get(1).equals("MIA"));
        Assert.assertTrue(destinations.get(2).equals("BUE"));
        Assert.assertTrue(destinations.get(3).equals("RIO"));
        Assert.assertTrue(destinations.get(4).equals("GRU"));
        Assert.assertTrue(destinations.get(5).equals("LIM"));
        Assert.assertTrue(destinations.get(6).equals("NYC"));
        Assert.assertTrue(destinations.get(7).equals("CUN"));
        Assert.assertTrue(destinations.get(8).equals("SCL"));
        Assert.assertTrue(destinations.get(9).equals("AAA"));
        Assert.assertTrue(destinations.get(10).equals("BBB"));
        Assert.assertTrue(destinations.get(11).equals("CCC"));
    }

    @Test
    public void testGetDestinations3() {

        List<Recommendation> recList = Arrays.asList(new Recommendation("ORL", 10), new Recommendation("MIA", 9));
        Mockito.when(
            this.rs.recommend(Mockito.eq(Product.HOTELS), Mockito.eq(Flow.THANKS), Mockito.eq(Product.HOTELS),
                Mockito.eq(Flow.THANKS), Mockito.anyString(), Mockito.eq(12))).thenReturn(recList);
        List<Recommendation> nearbycities = Arrays.asList(new Recommendation("BUE", 10), new Recommendation("RIO", 9),
            new Recommendation("GRU", 10), new Recommendation("LIM", 9), new Recommendation("NYC", 10));
        Mockito.when(
            this.rs.recommend(Mockito.eq(Product.HOTELS), Mockito.eq(Flow.SEARCH), Mockito.eq(Product.HOTELS),
                Mockito.eq(Flow.SEARCH), Mockito.anyString(), Mockito.eq(10))).thenReturn(nearbycities);

        RankingTreeDTO ranking = new RankingTreeDTO();
        ranking.addPosition(new RankingPositionDTO("CUN", 2l));
        ranking.addPosition(new RankingPositionDTO("SCL", 2l));
        ranking.addPosition(new RankingPositionDTO("AAA", 2l));
        ranking.addPosition(new RankingPositionDTO("BBB", 2l));
        ranking.addPosition(new RankingPositionDTO("CCC", 2l));

        Mockito.when(
            this.rankingService.getRankingFromIp(Mockito.eq(Product.HOTELS),  Mockito.anyString(),
                Mockito.eq(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_ANY), Mockito.eq(BaseFunction.RANKING_SIZE)))
            .thenReturn(ranking);

        List<String> destinations = this.function.getItemIds(this.context);
        Assert.assertNotNull(destinations);
        Assert.assertTrue(destinations.size() == 12);
        Assert.assertTrue(destinations.get(0).equals("ORL"));
        Assert.assertTrue(destinations.get(1).equals("MIA"));
        Assert.assertTrue(destinations.get(2).equals("BUE"));
        Assert.assertTrue(destinations.get(3).equals("RIO"));
        Assert.assertTrue(destinations.get(4).equals("GRU"));
        Assert.assertTrue(destinations.get(5).equals("LIM"));
        Assert.assertTrue(destinations.get(6).equals("NYC"));
        Assert.assertTrue(destinations.get(7).equals("CUN"));
        Assert.assertTrue(destinations.get(8).equals("SCL"));
        Assert.assertTrue(destinations.get(9).equals("AAA"));
        Assert.assertTrue(destinations.get(10).equals("BBB"));
        Assert.assertTrue(destinations.get(11).equals("CCC"));
    }

    @Test
    public void testGetDestinationsReturnNull() {

        List<Recommendation> recList = Arrays.asList(new Recommendation("ORL", 10), new Recommendation("MIA", 9));
        Mockito.when(
            this.rs.recommend(Mockito.eq(Product.HOTELS), Mockito.eq(Flow.THANKS), Mockito.eq(Product.HOTELS),
                Mockito.eq(Flow.THANKS), Mockito.anyString(), Mockito.eq(9))).thenReturn(recList);
        List<String> nearbycities = Lists.newArrayList("BUE", "RIO", "GRU", "LIM", "NYC");
        Mockito.when(this.geoService.getNearbyCities(Product.HOTELS, "MIA", 7)).thenReturn(nearbycities);

        RankingTreeDTO ranking = new RankingTreeDTO();
        ranking.addPosition(new RankingPositionDTO("CUN", 2l));

        Mockito.when(
            this.rankingService.getRankingFromIp(Mockito.eq(Product.HOTELS),  Mockito.anyString(),
                Mockito.eq(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_ANY), Mockito.eq(BaseFunction.RANKING_SIZE)))
            .thenReturn(ranking);

        List<String> destinations = this.function.getItemIds(this.context);
        Assert.assertNull(destinations);
    }

    @Test
    public void testGetDestinationsWithRepeatedValues() {

        List<Recommendation> recList = Arrays.asList(new Recommendation("ORL", 10), new Recommendation("MIA", 9));
        Mockito.when(
            this.rs.recommend(Mockito.eq(Product.HOTELS), Mockito.eq(Flow.THANKS), Mockito.eq(Product.HOTELS),
                Mockito.eq(Flow.THANKS), Mockito.anyString(), Mockito.eq(12))).thenReturn(recList);
        List<Recommendation> nearbycities = Arrays.asList(new Recommendation("BUE", 10), new Recommendation("RIO", 9),
            new Recommendation("GRU", 10), new Recommendation("LIM", 9), new Recommendation("NYC", 10));
        Mockito.when(
            this.rs.recommend(Mockito.eq(Product.HOTELS), Mockito.eq(Flow.SEARCH), Mockito.eq(Product.HOTELS),
                Mockito.eq(Flow.SEARCH), Mockito.anyString(), Mockito.eq(10))).thenReturn(nearbycities);

        RankingTreeDTO ranking = new RankingTreeDTO();
        ranking.addPosition(new RankingPositionDTO("CUN", 2l));
        ranking.addPosition(new RankingPositionDTO("SCL", 2l));
        ranking.addPosition(new RankingPositionDTO("AAA", 2l));
        ranking.addPosition(new RankingPositionDTO("BBB", 2l));
        ranking.addPosition(new RankingPositionDTO("CCC", 2l));

        Mockito.when(
            this.rankingService.getRankingFromIp(Mockito.eq(Product.HOTELS), Mockito.anyString(),
                Mockito.eq(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_ANY), Mockito.eq(BaseFunction.RANKING_SIZE)))
            .thenReturn(ranking);

        List<String> destinations = this.function.getItemIds(this.context);
        Assert.assertNotNull(destinations);
        Assert.assertTrue(destinations.size() == 12);
        Assert.assertTrue(destinations.get(0).equals("ORL"));
        Assert.assertTrue(destinations.get(1).equals("MIA"));
        Assert.assertTrue(destinations.get(2).equals("BUE"));
        Assert.assertTrue(destinations.get(3).equals("RIO"));
        Assert.assertTrue(destinations.get(4).equals("GRU"));
        Assert.assertTrue(destinations.get(5).equals("LIM"));
        Assert.assertTrue(destinations.get(6).equals("NYC"));
        Assert.assertTrue(destinations.get(7).equals("CUN"));
        Assert.assertTrue(destinations.get(8).equals("SCL"));
        Assert.assertTrue(destinations.get(9).equals("AAA"));
        Assert.assertTrue(destinations.get(10).equals("BBB"));
        Assert.assertTrue(destinations.get(11).equals("CCC"));
    }

}
