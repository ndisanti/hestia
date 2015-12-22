package ar.com.despegar.p13n.hestia;

import java.util.List;

import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.IntenseSearch;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.UserAction;
import com.despegar.p13n.euler.commons.client.model.UserContext;
import com.despegar.p13n.euler.commons.client.model.UserRecord;
import com.despegar.p13n.euler.commons.client.model.data.ProductData;
import com.despegar.p13n.hestia.api.data.model.HomeContent;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.SearchBoxRecommender;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.activity.BuyActivity;
import com.despegar.p13n.hestia.recommend.allinone.activity.SearchActivity;
import com.despegar.p13n.hestia.recommend.allinone.activity.UserActivity;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class SearchBoxRecommenderTest {



    private SearchBoxRecommender recommender = new SearchBoxRecommender();
    @Mock
    private ActionRecommendation action;
    @Mock
    private UserContext userContextAccesor;
    @Mock
    private ProductData data;
    @Mock
    private UserAction userAction;
    @Mock
    private UserRecord userPrifileRecord;

    @Before
    public void setUp() {
        Mockito.when(this.action.getCountryCode()).thenReturn(CountryCode.AR);
    }

    @Test
    public void testRecommendForNoHistory() {

        Mockito.when(this.action.getActivityType()).thenReturn(ActivityType.NO_HISTORY);
        Mockito.when(this.action.getNoHistoryRecommendedHome()).thenReturn(Product.CLOSED_PACKAGES);
        HomeContent homeContent = new HomeContent();
        this.recommender.recommendHome(homeContent, this.action);
        Assert.assertTrue(homeContent.getRecommendedHome().equals(Product.CLOSED_PACKAGES));
    }

    @Test
    public void testRecommendForLastResort() {

        Mockito.when(this.action.getActivityType()).thenReturn(ActivityType.LAST_RSRT);
        HomeContent homeContent = new HomeContent();
        this.recommender.recommendHome(homeContent, this.action);
        Assert.assertTrue(homeContent.getRecommendedHome().equals(Product.HOTELS));
    }

    @Test
    public void testRecommendForSearch() {

        SearchActivity searchActivity = Mockito.mock(SearchActivity.class);
        UserActivity userActivity = Mockito.mock(UserActivity.class);
        Mockito.when(this.action.getActivityType()).thenReturn(ActivityType.SEARCH);
        List<ProductData> history = Lists.newArrayList(this.data);
        Mockito.when(this.action.getHistory()).thenReturn(history);
        Mockito.when(this.action.getUserContext()).thenReturn(this.userContextAccesor);
        List<ProductData> dataList = Lists.newArrayList(this.data);
        Mockito.when(this.userContextAccesor.getProductDataList(Lists.newArrayList(Flow.CHECKOUT))).thenReturn(dataList);
        Mockito.when(this.data.getParent()).thenReturn(this.userAction);
        Mockito.when(this.userAction.getProduct()).thenReturn(Product.CARS);
        Mockito.when(searchActivity.getLastActivity()).thenReturn(userActivity);
        Mockito.when(userActivity.getProduct()).thenReturn(Product.CARS);
        HomeContent homeContent = new HomeContent();
        this.recommender.recommendHome(homeContent, this.action);
        Assert.assertTrue(homeContent.getRecommendedHome().equals(Product.CARS));
    }

    @Test
    public void testRecommendForBuyNoIntenseSearch() {

        BuyActivity buyActivity = Mockito.mock(BuyActivity.class);
        Mockito.when(this.action.getActivityType()).thenReturn(ActivityType.BUY);
        Mockito.when(this.action.getBuyActivity()).thenReturn(buyActivity);
        Mockito.when(this.action.getUserContext()).thenReturn(this.userContextAccesor);
        Mockito.when(this.userContextAccesor.getUserRecord()).thenReturn(this.userPrifileRecord);
        Mockito.when(this.userPrifileRecord.getLastIntenseSearch()).thenReturn(null);
        Mockito.when(buyActivity.getProduct()).thenReturn(Product.FLIGHTS);
        HomeContent homeContent = new HomeContent();
        this.recommender.recommendHome(homeContent, this.action);
        Assert.assertTrue(homeContent.getRecommendedHome().equals(Product.HOTELS));
    }

    @Test
    public void testRecommendForESIntenseSearch() {

        BuyActivity buyActivity = Mockito.mock(BuyActivity.class);

        Mockito.when(this.action.getCountryCode()).thenReturn(CountryCode.ES);
        Mockito.when(this.action.getActivityType()).thenReturn(ActivityType.BUY);
        Mockito.when(this.action.getBuyActivity()).thenReturn(buyActivity);
        List<ProductData> history = Lists.newArrayList(this.data);
        Mockito.when(this.action.getHistory()).thenReturn(history);
        Mockito.when(this.action.getUserContext()).thenReturn(this.userContextAccesor);
        List<ProductData> dataList = Lists.newArrayList(this.data);
        Mockito.when(this.userContextAccesor.getProductDataList(Lists.newArrayList(Flow.THANKS))).thenReturn(dataList);
        Mockito.when(this.data.getParent()).thenReturn(this.userAction);
        Mockito.when(this.userAction.getProduct()).thenReturn(Product.CARS);
        Mockito.when(this.action.getUserContext()).thenReturn(this.userContextAccesor);
        Mockito.when(this.userContextAccesor.getUserRecord()).thenReturn(this.userPrifileRecord);
        IntenseSearch intenseSearch = new IntenseSearch("BUE", "MIA", Product.CARS, CountryCode.US);
        Mockito.when(this.userPrifileRecord.getLastIntenseSearch()).thenReturn(intenseSearch);
        Mockito.when(buyActivity.getProduct()).thenReturn(Product.ACTIVITIES);
        HomeContent homeContent = new HomeContent();
        this.recommender.recommendHome(homeContent, this.action);
        Assert.assertTrue(homeContent.getRecommendedHome().equals(Product.HOTELS));
    }

}
