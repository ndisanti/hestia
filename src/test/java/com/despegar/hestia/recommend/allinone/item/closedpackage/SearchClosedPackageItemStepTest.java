package com.despegar.hestia.recommend.allinone.item.closedpackage;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ar.com.despegar.p13n.hestia.TestFuntions;

import com.despegar.hestia.test.TestUtils;
import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.UserAction;
import com.despegar.p13n.euler.commons.client.model.UserContext;
import com.despegar.p13n.euler.commons.client.model.WishList;
import com.despegar.p13n.euler.commons.client.model.WishListClosedPackageEntry;
import com.despegar.p13n.euler.commons.client.model.WishListEntry;
import com.despegar.p13n.euler.commons.client.model.recommendations.Recommendation;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.RankingTreeDTO;
import com.despegar.p13n.hestia.client.RankingsClient;
import com.despegar.p13n.hestia.client.RecommendationsClient;
import com.despegar.p13n.hestia.data.hbase.hot.types.StaticRankingTypes;
import com.despegar.p13n.hestia.external.geo.GeoService;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.SearchActivity;
import com.despegar.p13n.hestia.recommend.allinone.activity.UserActivity;
import com.despegar.p13n.hestia.recommend.allinone.item.closedpackage.SearchClosedPackageItemStep;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.BaseFunction;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

@RunWith(MockitoJUnitRunner.class)
public class SearchClosedPackageItemStepTest {

	@InjectMocks
    private SearchClosedPackageItemStep steps = new SearchClosedPackageItemStep();
    @Mock
    private RankingsClient hotRankingService;
    @Mock
    private RecommendationsClient recommendation;
    @Mock
    private GeoService geoService;

    @Test
    public void testGetItem() {
    	Mockito.when(this.geoService.normalizeIata(Mockito.anyString())).thenReturn("MIA");
    	Mockito.when(this.geoService.normalizeToCityAirport(Mockito.anyString())).thenReturn("BUE");
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Detail(Product.CLOSED_PACKAGES, "MIA", "CLUID");
        SearchActivity searchActivity = Mockito.mock(SearchActivity.class);
        action.setSearchActivity(searchActivity);
        UserActivity userActivity = Mockito.mock(UserActivity.class);
        UserAction userAction = new UserAction();
        userAction.setFlow(Flow.DETAIL);
        Mockito.when(userActivity.getAction()).thenReturn(userAction);
        Mockito.when(userActivity.getDestination()).thenReturn("MIA");
        WishList wishList = new WishList();
        wishList.processLike(this.createWishListClosedPackageEntry("CLUID1"));
        UserContext userContext = Mockito.mock(UserContext.class);
        action.setUserContext(userContext);
        Mockito.when(userContext.getWishlist()).thenReturn(wishList);
        Mockito.when(searchActivity.getActivity(Product.CLOSED_PACKAGES)).thenReturn(userActivity);
        ItemHome item = this.steps.execute("MIA", action);
        Assert.assertNotNull(item);
        Assert.assertTrue(item.getId().equals("CLUID1"));
        Multimap<String, String> cpBydestination = LinkedHashMultimap.create();
        cpBydestination.put("MIA", "CLUID2");
        Mockito.when(searchActivity.getLastClosedPackagesByDestination()).thenReturn(cpBydestination);
        item = this.steps.execute("MIA", action);
        Assert.assertNotNull(item);
        Assert.assertTrue(item.getId().equals("CLUID2"));
        List<Recommendation> recommendations = new ArrayList<Recommendation>();
        recommendations.add(new Recommendation("CLUID3", 44));
        Mockito.when(
            this.recommendation.recommend(Mockito.eq(Product.CLOSED_PACKAGES), Mockito.eq(Flow.DETAIL),
                Mockito.eq(Product.CLOSED_PACKAGES), Mockito.eq(Flow.DETAIL), Mockito.anyString(),
                Mockito.eq(BaseFunction.RANKING_SIZE))).thenReturn(recommendations);
        item = this.steps.execute("MIA", action);
        Assert.assertNotNull(item);
        Assert.assertTrue(item.getId().equals("CLUID3"));
        userAction.setFlow(Flow.THANKS);
        List<Pair<String, Long>> recs = new ArrayList<Pair<String, Long>>();
        recs.add(Pair.of("CLUID4", 2L));
        RankingTreeDTO ranking = TestUtils.createRankingTreeDTO(recs);
        Mockito.when(
            this.hotRankingService.getRankingFromIataNoFallback(Product.CLOSED_PACKAGES, "MIA",
                StaticRankingTypes.PACKAGE_DETAIL_DESTINATION, BaseFunction.RANKING_SIZE)).thenReturn(ranking);
        item = this.steps.execute("MIA", action);
        Assert.assertNotNull(item);
        Assert.assertTrue(item.getId().equals("CLUID4"));
        action.setOrigin("BUE");
        ItemHome itemDestination = this.steps.execute("MIA", action);
        Assert.assertNotNull(itemDestination);
        Assert.assertTrue(itemDestination.getId().equals("MIA"));
    }

    private WishListEntry createWishListClosedPackageEntry(String cluid) {
        WishListClosedPackageEntry entry = new WishListClosedPackageEntry(cluid);
        entry.setProduct(Product.CLOSED_PACKAGES);
        return entry;
    }

}
