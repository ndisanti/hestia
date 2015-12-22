package com.despegar.p13n.hestia.recommend.allinone.rules.destination.functions;

import java.util.ArrayList;
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
import com.despegar.p13n.euler.commons.client.model.recommendations.Recommendation;
import com.despegar.p13n.hestia.api.data.model.RankingPositionDTO;
import com.despegar.p13n.hestia.api.data.model.RankingTreeDTO;
import com.despegar.p13n.hestia.client.RankingsClient;
import com.despegar.p13n.hestia.client.RecommendationsClient;
import com.despegar.p13n.hestia.data.hbase.hot.types.StaticRankingTypes;
import com.despegar.p13n.hestia.external.geo.GeoService;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.SearchActivity;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.BaseFunction;

@RunWith(MockitoJUnitRunner.class)
public class SearchDestinationsFunctionTest {

	@InjectMocks
    private SearchDestinationsFunction function = new SearchDestinationsFunction();
    @Mock
    private RecommendationsClient rs;
    @Mock
    private GeoService geoService;
    @Mock
    private RankingsClient rankingService;
    @Mock
    private SearchActivity searchActivity;
    @Mock
    private ActionRecommendation context;

    @Before
    public void setUp() {
        Mockito.when(this.context.getCurrentHome()).thenReturn(Product.HOTELS);
        List<String> lastDestinations = new ArrayList<String>();
        lastDestinations.add("MIA");
        lastDestinations.add("BCN");
        Mockito.when(this.searchActivity.getLastDestinations()).thenReturn(lastDestinations);
        Mockito.when(this.context.getSearchActivity()).thenReturn(this.searchActivity);
        Mockito.when(this.context.getCountryCode()).thenReturn(CountryCode.AR);
    }

    @Test
    public void testGetDestinations() {


        List<Recommendation> recList = Arrays.asList(new Recommendation("a", 10), new Recommendation("b", 9),
            new Recommendation("c", 8), new Recommendation("d", 7), new Recommendation("e", 6));

        Mockito.when(
            this.rs.recommend(Mockito.eq(Product.HOTELS), Mockito.eq(Flow.SEARCH), Mockito.eq(Product.HOTELS),
                Mockito.eq(Flow.SEARCH), Mockito.anyString(), Mockito.eq(8))).thenReturn(recList);

        List<Recommendation> recList2 = Arrays.asList(new Recommendation("f", 5), new Recommendation("z", 10),
            new Recommendation("zz", 10), new Recommendation("zzz", 10), new Recommendation("zzzz", 10));

        Mockito.when(
            this.rs.recommend(Mockito.eq(Product.HOTELS), Mockito.eq(Flow.SEARCH), Mockito.eq(Product.HOTELS),
                Mockito.eq(Flow.SEARCH), Mockito.anyString(), Mockito.eq(3))).thenReturn(recList2);

        List<String> destinations = this.function.getItemIds(this.context);

        Assert.assertNotNull(destinations);
        Assert.assertTrue(destinations.size() == 12);
        Assert.assertTrue(destinations.get(0).equals("MIA"));
        Assert.assertTrue(destinations.get(1).equals("BCN"));
        Assert.assertTrue(destinations.get(2).equals("a"));
        Assert.assertTrue(destinations.get(3).equals("b"));
        Assert.assertTrue(destinations.get(4).equals("c"));
        Assert.assertTrue(destinations.get(5).equals("d"));
        Assert.assertTrue(destinations.get(6).equals("e"));
        Assert.assertTrue(destinations.get(7).equals("f"));
        Assert.assertTrue(destinations.get(8).equals("z"));
        Assert.assertTrue(destinations.get(9).equals("zz"));
        Assert.assertTrue(destinations.get(10).equals("zzz"));
        Assert.assertTrue(destinations.get(11).equals("zzzz"));
    }

    @Test
    public void testGetDestinationswithRankingService() {

        RankingTreeDTO anotherRanking = new RankingTreeDTO();

        anotherRanking.addPosition(new RankingPositionDTO("aaa", 2l));
        anotherRanking.addPosition(new RankingPositionDTO("bbb", 2l));
        anotherRanking.addPosition(new RankingPositionDTO("ccc", 2l));
        anotherRanking.addPosition(new RankingPositionDTO("ddd", 2l));
        anotherRanking.addPosition(new RankingPositionDTO("zz", 2l));
        anotherRanking.addPosition(new RankingPositionDTO("zzz", 2l));
        anotherRanking.addPosition(new RankingPositionDTO("zzzz", 2l));
        List<Recommendation> recList = Arrays.asList(new Recommendation("a", 10), new Recommendation("b", 9));

        Mockito.when(
            this.rs.recommend(Mockito.eq(Product.HOTELS), Mockito.eq(Flow.SEARCH), Mockito.eq(Product.HOTELS),
                Mockito.eq(Flow.SEARCH), Mockito.anyString(), Mockito.eq(10))).thenReturn(recList);

        List<Recommendation> recList2 = Arrays.asList(new Recommendation("c", 10));

        Mockito.when(
            this.rs.recommend(Mockito.eq(Product.HOTELS), Mockito.eq(Flow.SEARCH), Mockito.eq(Product.HOTELS),
                Mockito.eq(Flow.SEARCH), Mockito.anyString(), Mockito.eq(8))).thenReturn(recList2);

        
        Mockito.when(
        		this.rankingService.getRanking(Mockito.any(Product.class), Mockito.eq(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_ANY), Mockito.eq(CountryCode.AR), Mockito.eq(BaseFunction.RANKING_SIZE)))
            .thenReturn(anotherRanking);

        List<String> destinations = this.function.getItemIds(this.context);

        Assert.assertNotNull(destinations);
        Assert.assertTrue(destinations.size() == 10);
        Assert.assertTrue(destinations.get(0).equals("MIA"));
        Assert.assertTrue(destinations.get(1).equals("BCN"));
        Assert.assertTrue(destinations.get(2).equals("c"));
        Assert.assertTrue(destinations.get(3).equals("aaa"));
        Assert.assertTrue(destinations.get(4).equals("bbb"));
        Assert.assertTrue(destinations.get(5).equals("ccc"));
        Assert.assertTrue(destinations.get(6).equals("ddd"));
        Assert.assertTrue(destinations.get(7).equals("zz"));
        Assert.assertTrue(destinations.get(8).equals("zzz"));
        Assert.assertTrue(destinations.get(9).equals("zzzz"));
    }

    @Test
    public void testGetDestinationsReturnNull() {

        RankingTreeDTO anotherRanking = new RankingTreeDTO();

        anotherRanking.addPosition(new RankingPositionDTO("aaa", 2l));

        Mockito.when(
        		this.rankingService.getRanking(Mockito.any(Product.class), Mockito.eq(StaticRankingTypes.HOT_SEARCHES_DESTINATIONS_ANY), Mockito.eq(CountryCode.AR), Mockito.eq(BaseFunction.RANKING_SIZE)))
            .thenReturn(anotherRanking);

        List<Recommendation> recList = Arrays.asList(new Recommendation("a", 10), new Recommendation("b", 9),
            new Recommendation("b", 8), new Recommendation("d", 7), new Recommendation("d", 6), new Recommendation("f", 5));

        Mockito.when(
            this.rs.recommend(Mockito.eq(Product.HOTELS), Mockito.eq(Flow.SEARCH), Mockito.eq(Product.HOTELS),
                Mockito.eq(Flow.SEARCH), Mockito.anyString(), Mockito.eq(10))).thenReturn(recList);
        List<Recommendation> recList2 = Arrays.asList(new Recommendation("z", 10));
        Mockito.when(
            this.rs.recommend(Mockito.eq(Product.HOTELS), Mockito.eq(Flow.SEARCH), Mockito.eq(Product.HOTELS),
                Mockito.eq(Flow.SEARCH), Mockito.anyString(), Mockito.eq(6))).thenReturn(recList2);

        List<String> destinations = this.function.getItemIds(this.context);

        Assert.assertNull(destinations);

    }
}
