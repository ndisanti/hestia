package ar.com.despegar.p13n.hestia;

import java.net.InetAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;

import org.joda.time.DateTimeUtils;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.despegar.p13n.euler.commons.client.UserContextClient;
import com.despegar.p13n.euler.commons.client.UserContextClientParameters;
import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Language;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.UserAction;
import com.despegar.p13n.euler.commons.client.model.UserContext;
import com.despegar.p13n.euler.commons.client.model.UserLocation;
import com.despegar.p13n.euler.commons.client.model.UserRecord;
import com.despegar.p13n.euler.commons.client.model.data.HotelData;
import com.despegar.p13n.euler.commons.client.model.data.ProductData;
import com.despegar.p13n.hestia.api.data.model.SearchAction;
import com.despegar.p13n.hestia.external.geo.GeoService;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendationBuilder;
import com.despegar.p13n.hestia.recommend.allinone.HomeParam;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.activity.Anticipation;
import com.despegar.p13n.hestia.recommend.allinone.activity.LastAction;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.net.InetAddresses;

@RunWith(MockitoJUnitRunner.class)
public class ActionRecommendationBuilderTest extends MockitoAnnotationBaseTest {

    /**
     * 
     */
    private static final InetAddress IP = InetAddresses.forString("123.123.123.123");

    @Mock
    private GeoService geoService;
    @Mock
    private UserContextClient usercontextClient;
    @InjectMocks
    private ActionRecommendationBuilder builder = new ActionRecommendationBuilder();
  
    @Mock
	private UserContext userContext;
    @Mock
	private UserRecord userRecord;

    @Before
    public void before() {

        UserLocation userLocation = new UserLocation();
        userLocation.setCountryCode(CountryCode.AR.name());
        userLocation.setCity("BUE");
        userLocation.setIp(IP.toString());
        Mockito.when(this.usercontextClient.getUserContext(Mockito.any(UserContextClientParameters.class))).thenReturn(userContext);
        Mockito.when(this.userContext.getUserLocation()).thenReturn(userLocation);
        Mockito.when(this.userContext.getUserRecord()).thenReturn(this.userRecord);
        Mockito.when(this.userRecord.getLastIntenseSearch()).thenReturn(null);
    }

    @After
    public void after() {
        DateTimeUtils.setCurrentMillisSystem();
    }

    @Test
    public void testSearch() {
        SearchAction searchAction = this.buildSearchAction();

        DateTimeUtils.setCurrentMillisFixed(searchAction.getTimestamp() + TimeUnit.DAYS.toMillis(2));

        // search: 2013-03-01
        // now: 2013-03-03
        // checkin: 2013-03-10

        List<UserAction> actions = Lists.newArrayList();
        actions.add(searchAction);

        Mockito.when(this.userContext.getProductDataList()).thenReturn(this.asProductDataList(actions));
        ActionRecommendation action = this.builder.buildActionRecommendation(new HomeParam(CountryCode.AR, IP,
            Product.HOTELS, "12345", Language.ES));

        action.setUserContext(this.userContext);
        this.builder.populateUserContext(action);

        Assert.assertEquals(ActivityType.SEARCH, action.getActivityType());
        Assert.assertNull(action.getBuyActivity());
        Assert.assertEquals("MIA", action.getSearchActivity().getLastActivity().getDestination());
        Assert.assertEquals(7, action.getAnticipationDays(Product.HOTELS));
        Assert.assertEquals(2, action.getLastActionDays(Product.HOTELS));

    }


    /**
     * LastAction:    search
     * PreviousAction: detail
     * 
     * Details overrides search
     */
    @Test
    public void testDetailOverridesSearch() {
        SearchAction searchAction = this.buildSearchAction();

        DateTimeUtils.setCurrentMillisFixed(searchAction.getTimestamp() + TimeUnit.DAYS.toMillis(2));

        // detail: 2013-02-28
        // search: 2013-03-01
        // now: 2013-03-03
        // checkin: 2013-03-10

        List<UserAction> actions = Lists.newArrayList();
        actions.add(searchAction);
        
        Mockito.when(this.userContext.getProductDataList()).thenReturn(this.asProductDataList(actions));

        ActionRecommendation action = this.builder.buildActionRecommendation(new HomeParam(CountryCode.AR, IP,
            Product.HOTELS, "12345", Language.ES));

        action.setUserContext(this.userContext);
        this.builder.populateUserContext(action);


        Assert.assertEquals(ActivityType.SEARCH, action.getActivityType());
        Assert.assertEquals(Flow.SEARCH, action.getSearchActivity().getLastActivity().getFlow());
        Assert.assertNull(action.getBuyActivity());
        Assert.assertEquals(7, action.getAnticipationDays(Product.HOTELS));
        Assert.assertEquals(2, action.getLastActionDays(Product.HOTELS));

    }

    @Test
    public void testGetBuyAndSearch() {
        SearchAction searchAction = this.buildSearchAction();

        DateTimeUtils.setCurrentMillisFixed(searchAction.getTimestamp() + TimeUnit.DAYS.toMillis(2));

        // thanks: 2013-02-28
        // search: 2013-03-01
        // now: 2013-03-03
        // checkin: 2013-03-10

        List<UserAction> actions = Lists.newArrayList();
        actions.add(searchAction);
        
        Mockito.when(this.userContext.getProductDataList()).thenReturn(this.asProductDataList(actions));

        ActionRecommendation action = this.builder.buildActionRecommendation(new HomeParam(CountryCode.AR, IP,
            Product.HOTELS, "12345", Language.ES));
        action.setUserContext(this.userContext);
        this.builder.populateUserContext(action);
        Assert.assertEquals(ActivityType.SEARCH, action.getActivityType());
        Assert.assertNotNull(action.getSearchActivity());
        Assert.assertEquals(7, action.getAnticipationDays(Product.HOTELS));
        Assert.assertEquals(2, action.getLastActionDays(Product.HOTELS));
    }

    @Test
    public void testNoHistory() {


        ActionRecommendation action = this.builder.buildActionRecommendation(new HomeParam(CountryCode.AR, IP,
            Product.HOTELS, "12345", Language.ES));

        Mockito.when(this.userContext.getProductDataList()).thenReturn(Lists.<ProductData> newArrayList());
        action.setUserContext(this.userContext);
        this.builder.populateUserContext(action);

        Assert.assertEquals(ActivityType.NO_HISTORY, action.getActivityType());
        Assert.assertNull(action.getSearchActivity());
        Assert.assertEquals(Anticipation.any(), action.getAnticipationDays(Product.HOTELS));
        Assert.assertEquals(LastAction.any(), action.getLastActionDays(Product.HOTELS));
    }

    private List<ProductData> asProductDataList(List<UserAction> actions) {
        List<ProductData> prDataList = Lists.newArrayList();

        for (UserAction action : actions) {
            prDataList.add(ProductData.create(action));
        }
        return prDataList;
    }

    private SearchAction buildSearchAction() {
        SearchAction searchAction = new SearchAction();
        searchAction.setUserId("12345");
        searchAction.setProduct(Product.HOTELS);
        searchAction.setFlow(Flow.SEARCH);
        searchAction.setTimestamp(new LocalDate(2013, 3, 1).toDate().getTime());

        Map<String, Object> data = Maps.newHashMap();
        data.put(HotelData.HOTEL_ID, "1");
        data.put(HotelData.CHECKIN, "2013-03-10");
        data.put(HotelData.DESTINATION_CODE, "MIA");
        searchAction.setActionData(data);
        return searchAction;
    }
}
