/**
 * 
 */
package ar.com.despegar.p13n.hestia;

import java.util.List;

import org.junit.Assert;
import org.mockito.Mockito;

import com.despegar.hestia.api.data.model.DetailAction;
import com.despegar.hestia.api.data.model.ThanksAction;
import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Language;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.UserAction;
import com.despegar.p13n.euler.commons.client.model.UserContext;
import com.despegar.p13n.euler.commons.client.model.UserLocation;
import com.despegar.p13n.euler.commons.client.model.data.CruiseData;
import com.despegar.p13n.euler.commons.client.model.data.ProductData;
import com.despegar.p13n.hestia.api.data.model.ActivityDestinationItem;
import com.despegar.p13n.hestia.api.data.model.ActivityItem;
import com.despegar.p13n.hestia.api.data.model.CarDestinationItem;
import com.despegar.p13n.hestia.api.data.model.CruiseRegionItem;
import com.despegar.p13n.hestia.api.data.model.FlightDestinationItem;
import com.despegar.p13n.hestia.api.data.model.HotelDestinationItem;
import com.despegar.p13n.hestia.api.data.model.HotelItem;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.ItemType;
import com.despegar.p13n.hestia.api.data.model.PriceCarCategoryItem;
import com.despegar.p13n.hestia.api.data.model.RulesVersion;
import com.despegar.p13n.hestia.api.data.model.SearchAction;
import com.despegar.p13n.hestia.api.data.model.VacationRentalItem;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.BuyActivity;
import com.despegar.p13n.hestia.recommend.allinone.activity.SearchActivity;
import com.despegar.p13n.hestia.recommend.allinone.activity.UserActivity;
import com.despegar.p13n.hestia.recommend.allinone.matrix.SearchCount;
import com.despegar.p13n.hestia.recommend.allinone.rules.SectionsEnum;
import com.despegar.p13n.hestia.recommend.allinone.title.TitleData;
import com.google.common.collect.Lists;
import com.google.common.net.InetAddresses;

/**
 *
 */
public class TestFuntions {

    public static ActionRecommendation buildActionRecommendation4Buy(final Product buyProduct, final String destination) {
        final ActionRecommendation context = new ActionRecommendation("", Product.HOTELS, CountryCode.AR, Language.ES,
            InetAddresses.forString("123.123.123.123"), RulesVersion.DYNAMIC_SERVICE, null, null);

        ThanksAction thanksAction = new ThanksAction();
        thanksAction.setUserId("12345");
        thanksAction.setProduct(buyProduct);
        thanksAction.setFlow(Flow.THANKS);

        UserActivity lastActivity = new UserActivity(2, destination, "", 2, thanksAction, SearchCount.ONE_TO_THREE);

        context.setBuyActivity(new BuyActivity(buyProduct, lastActivity));
        context.setCurrentSection(SectionsEnum.OFFER);
        context.setTitleData(new TitleData());
        context.setUserContext(mockUserContextAccesor(Lists.<UserAction> newArrayList(thanksAction)));
        context.setForceRulesversion(RulesVersion.DYNAMIC_SERVICE);

        return context;
    }

    public static ActionRecommendation buildActionRecommendation4SearchAndViewed(final Product searchProduct,
        final String destination, String id) {
        final ActionRecommendation context = new ActionRecommendation("12345", Product.HOTELS, CountryCode.AR, Language.ES,
            InetAddresses.forString("123.123.123.123"), RulesVersion.DYNAMIC_SERVICE, null, null);

        SearchAction searchAction = new SearchAction();
        searchAction.setUserId("12345");
        searchAction.setProduct(searchProduct);
        searchAction.setFlow(Flow.SEARCH);

        UserActivity lastActivity = new UserActivity(2, destination, id, 2, searchAction, SearchCount.ONE_TO_THREE);

        SearchActivity searchActivity = new SearchActivity();
        searchActivity.addSearchActivity(searchProduct, lastActivity);

        context.setSearchActivity(searchActivity);
        context.setCurrentSection(SectionsEnum.OFFER);
        context.setTitleData(new TitleData());
        context.setUserContext(mockUserContextAccesor(Lists.<UserAction> newArrayList(searchAction)));
        return context;
    }

    public static ActionRecommendation buildActionRecommendation4DetailAndViewed(final Product searchProduct,
        final String destination, String id) {
        final ActionRecommendation context = new ActionRecommendation("12345", Product.HOTELS, CountryCode.AR, Language.ES,
            InetAddresses.forString("123.123.123.123"), RulesVersion.DYNAMIC_SERVICE, null, null);

        SearchAction searchAction = new SearchAction();
        searchAction.setUserId("12345");
        searchAction.setProduct(searchProduct);
        searchAction.setFlow(Flow.DETAIL);

        UserActivity lastActivity = new UserActivity(2, destination, id, 2, searchAction, SearchCount.ONE_TO_THREE);

        SearchActivity searchActivity = new SearchActivity();
        searchActivity.addSearchActivity(searchProduct, lastActivity);

        context.setSearchActivity(searchActivity);
        context.setCurrentSection(SectionsEnum.OFFER);
        context.setTitleData(new TitleData());
        context.setUserContext(mockUserContextAccesor(Lists.<UserAction> newArrayList(searchAction)));
        return context;
    }

    public static ActionRecommendation buildActionRecommendation4Search(final Product searchProduct, final String destination) {
        final ActionRecommendation context = new ActionRecommendation("12345", Product.HOTELS, CountryCode.AR, Language.ES,
            InetAddresses.forString("123.123.123.123"), RulesVersion.DYNAMIC_SERVICE, null, null);

        SearchAction searchAction = new SearchAction();
        searchAction.setUserId("12345");
        searchAction.setProduct(searchProduct);
        searchAction.setFlow(Flow.SEARCH);

        UserActivity lastActivity = new UserActivity(2, destination, "", 2, searchAction, SearchCount.ONE_TO_THREE);

        SearchActivity searchActivity = new SearchActivity();
        searchActivity.addSearchActivity(searchProduct, lastActivity);

        context.setSearchActivity(searchActivity);
        context.setCurrentSection(SectionsEnum.OFFER);
        context.setTitleData(new TitleData());
        context.setUserContext(mockUserContextAccesor(Lists.<UserAction> newArrayList(searchAction)));
        context.setForceRulesversion(RulesVersion.DYNAMIC_SERVICE);
        return context;
    }

    public static ActionRecommendation buildActionRecommendation4Search(final Product homePr, final Product searchProduct,
        final String destination) {
        final ActionRecommendation context = new ActionRecommendation("12345", homePr, CountryCode.AR, Language.ES,
            InetAddresses.forString("123.123.123.123"), RulesVersion.DYNAMIC_SERVICE, null, null);

        SearchAction searchAction = new SearchAction();
        searchAction.setUserId("12345");
        searchAction.setProduct(searchProduct);
        searchAction.setFlow(Flow.SEARCH);

        UserActivity lastActivity = new UserActivity(2, destination, "", 2, searchAction, SearchCount.ONE_TO_THREE);

        SearchActivity searchActivity = new SearchActivity();
        searchActivity.addSearchActivity(searchProduct, lastActivity);

        context.setSearchActivity(searchActivity);
        context.setCurrentSection(SectionsEnum.OFFER);
        context.setTitleData(new TitleData());
        context.setUserContext(mockUserContextAccesor(Lists.<UserAction> newArrayList(searchAction)));
        return context;
    }

    public static ActionRecommendation buildActionRecommendation4Detail(final Product searchProduct,
        final String destination, final String pbId) {
        final ActionRecommendation context = new ActionRecommendation("12345", Product.HOTELS, CountryCode.AR, Language.ES,
            InetAddresses.forString("123.123.123.123"), RulesVersion.DYNAMIC_SERVICE, null, null);

        DetailAction detailAction = new DetailAction();
        detailAction.setUserId("12345");
        detailAction.setProduct(searchProduct);
        detailAction.setFlow(Flow.DETAIL);

        UserActivity lastActivity = new UserActivity(2, destination, pbId, 2, detailAction, SearchCount.ONE_TO_THREE);

        SearchActivity searchActivity = new SearchActivity();
        searchActivity.addSearchActivity(searchProduct, lastActivity);

        context.setSearchActivity(searchActivity);
        context.setCurrentSection(SectionsEnum.OFFER);
        context.setTitleData(new TitleData());
        context.setUserContext(mockUserContextAccesor(Lists.<UserAction> newArrayList(detailAction)));

        return context;
    }


    public static ActionRecommendation buildActionRecommendation4CruiseSearch(final String reg) {
        final ActionRecommendation context = new ActionRecommendation("", Product.CRUISES, CountryCode.AR, Language.ES,
            null, RulesVersion.DYNAMIC_SERVICE, null, null);

        SearchAction searchAction = new SearchAction();
        searchAction.setUserId("12345");
        searchAction.setProduct(Product.CRUISES);
        searchAction.getActionData().put(CruiseData.REGION, reg);
        searchAction.setFlow(Flow.SEARCH);


        UserActivity lastActivity = new UserActivity(2, "MIA", "", 2, searchAction, SearchCount.ONE_TO_THREE);

        SearchActivity searchActivity = new SearchActivity();
        searchActivity.addSearchActivity(Product.CRUISES, lastActivity);

        context.setSearchActivity(searchActivity);
        context.setCurrentSection(SectionsEnum.OFFER);
        context.setTitleData(new TitleData());
        context.setUserContext(mockUserContextAccesor(Lists.<UserAction> newArrayList(searchAction)));
        return context;
    }

    public static ActionRecommendation buildActionRecommendation4CruiseDetail(final String dest, String region, String did) {
        final ActionRecommendation context = new ActionRecommendation("", Product.CRUISES, CountryCode.AR, Language.ES,
            null, RulesVersion.DYNAMIC_SERVICE, null, null);

        DetailAction detailAction = new DetailAction();
        detailAction.setUserId("12345");
        detailAction.setProduct(Product.CRUISES);
        detailAction.getActionData().put(CruiseData.DID, did);
        detailAction.getActionData().put(CruiseData.REGION, region);
        detailAction.setFlow(Flow.DETAIL);

        UserActivity lastActivity = new UserActivity(2, dest, did, 2, detailAction, SearchCount.ONE_TO_THREE);

        SearchActivity searchActivity = new SearchActivity();
        searchActivity.addSearchActivity(Product.CRUISES, lastActivity);

        context.setSearchActivity(searchActivity);
        context.setCurrentSection(SectionsEnum.OFFER);
        context.setTitleData(new TitleData());
        context.setUserContext(mockUserContextAccesor());
        context.setUserContext(mockUserContextAccesor(Lists.<UserAction> newArrayList(detailAction)));

        return context;
    }

    public static ActionRecommendation buildActionRecommendation4CruiseThanks(final String id) {
        final ActionRecommendation context = new ActionRecommendation("", Product.CRUISES, CountryCode.AR, Language.ES,
            null, RulesVersion.DYNAMIC_SERVICE, null, null);

        ThanksAction thanksAction = new ThanksAction();
        thanksAction.setUserId("12345");
        thanksAction.setProduct(Product.CRUISES);
        thanksAction.getActionData().put(CruiseData.DID, id);
        thanksAction.setFlow(Flow.THANKS);

        UserActivity lastActivity = new UserActivity(2, "MIA", "", 2, thanksAction, SearchCount.NA);

        context.setBuyActivity(new BuyActivity(Product.CRUISES, lastActivity));
        context.setCurrentSection(SectionsEnum.OFFER);
        context.setTitleData(new TitleData());
        context.setUserContext(mockUserContextAccesor(Lists.<UserAction> newArrayList(thanksAction)));
        return context;
    }

    


    public static void assertHotelItem(ItemHome ih, String hotelId) {
        Assert.assertEquals(HotelItem.class, ih.getClass());
        Assert.assertEquals(ItemType.HOTEL, ih.getOfferType());
        HotelItem hotelItem = (HotelItem) ih;
        Assert.assertEquals(hotelId, hotelItem.getHid());
    }

    public static void assertRentalsItem(ItemHome ih, String vrid) {
        Assert.assertEquals(VacationRentalItem.class, ih.getClass());
        Assert.assertEquals(ItemType.VACATION_RENTAL, ih.getOfferType());
        VacationRentalItem rentalItem = (VacationRentalItem) ih;
        Assert.assertEquals(vrid, rentalItem.getVrid());
    }

    public static void assertHotelDestination(ItemHome ih, String destination) {
        Assert.assertEquals(HotelDestinationItem.class, ih.getClass());
        Assert.assertEquals(ItemType.HOTEL_DESTINATION, ih.getOfferType());
        HotelDestinationItem hotelDestinationItem = (HotelDestinationItem) ih;
        Assert.assertEquals(destination, hotelDestinationItem.getDestination());
    }

    public static void assertFlightlDestination(ItemHome ih, String origin, String destination) {
        Assert.assertEquals(FlightDestinationItem.class, ih.getClass());
        Assert.assertEquals(ItemType.FLIGHT_DESTINATION, ih.getOfferType());
        FlightDestinationItem flightDestinationItem = (FlightDestinationItem) ih;
        Assert.assertEquals(origin, flightDestinationItem.getOrigin());
        Assert.assertEquals(destination, flightDestinationItem.getDestination());
    }

    public static void assertCarCategory(ItemHome offer, String carcat, String dest, String pullType) {
        Assert.assertEquals(PriceCarCategoryItem.class, offer.getClass());
        Assert.assertEquals(ItemType.CAR_CATEGORY, offer.getOfferType());
        PriceCarCategoryItem priceCarCategoryItem = (PriceCarCategoryItem) offer;
        Assert.assertEquals(carcat, priceCarCategoryItem.getCarcat());
        Assert.assertEquals(dest, priceCarCategoryItem.getDestination());
        Assert.assertEquals(pullType, priceCarCategoryItem.getPulType());
        // Assert.assertEquals("tb1", priceCarCategoryItem.getPrice());
    }

    public static void assertCarDestinationItem(ItemHome offer, String carcat, String dest, String pullType) {
        Assert.assertEquals(CarDestinationItem.class, offer.getClass());
        Assert.assertEquals(ItemType.CAR_DESTINATION, offer.getOfferType());
        CarDestinationItem carDestinationItem = (CarDestinationItem) offer;
        Assert.assertEquals(dest, carDestinationItem.getDestination());
        Assert.assertEquals(pullType, carDestinationItem.getPulType());
        // Assert.assertEquals("tb1", priceCarCategoryItem.getPrice());
    }

    public static void assertCruiseRegionItem(ItemHome itemHome, String reg) {
        Assert.assertEquals(CruiseRegionItem.class, itemHome.getClass());
        Assert.assertEquals(ItemType.CRUISE_REGION, itemHome.getOfferType());
        CruiseRegionItem cruiseRegion = (CruiseRegionItem) itemHome;
        Assert.assertEquals(cruiseRegion.getReg(), reg);
    }

    public static void assertActivityItem(ItemHome ih, String actid) {
        Assert.assertEquals(ActivityItem.class, ih.getClass());
        Assert.assertEquals(ItemType.ACTIVITY, ih.getOfferType());
        ActivityItem activityItem = (ActivityItem) ih;
        Assert.assertEquals(actid, activityItem.getActid());
    }

    public static void assertActivityDestinationItem(ItemHome ih, String actid) {
        Assert.assertEquals(ActivityDestinationItem.class, ih.getClass());
        Assert.assertEquals(ItemType.ACTIVITY_DESTINATION, ih.getOfferType());
        ActivityDestinationItem activityDestinationItem = (ActivityDestinationItem) ih;
        Assert.assertEquals(actid, activityDestinationItem.getDestination());
    }

    private static UserContext mockUserContextAccesor() {
        return mockUserContextAccesor(Lists.<UserAction> newArrayList());
    }

    private static UserContext mockUserContextAccesor(List<UserAction> actions) {
        UserContext userContextAccesor = Mockito.mock(UserContext.class);
        UserLocation userLocation = new UserLocation();
        
        userLocation.setCity("BUE");
        userLocation.setCountryCode("AR");
        userLocation.setIp("123.123.123.123");
		Mockito.when(userContextAccesor.getUserLocation()).thenReturn(
            userLocation );
        List<ProductData> prDataList = Lists.newArrayList();

        for (UserAction action : actions) {
            prDataList.add(ProductData.create(action));
        }

        Mockito.when(userContextAccesor.getProductDataList()).thenReturn(prDataList);
        return userContextAccesor;
    }
}
