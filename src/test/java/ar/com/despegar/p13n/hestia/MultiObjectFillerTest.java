package ar.com.despegar.p13n.hestia;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.despegar.p13n.euler.commons.client.model.Corporate;
import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.EmailSource;
import com.despegar.p13n.euler.commons.client.model.Language;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.UserAction;
import com.despegar.p13n.euler.commons.client.model.UserContext;
import com.despegar.p13n.euler.commons.client.model.UserLocation;
import com.despegar.p13n.euler.commons.client.model.UserRecord;
import com.despegar.p13n.euler.commons.client.model.UserRecord.UserRecordBuilder;
import com.despegar.p13n.hestia.api.data.model.CarDestinationItem;
import com.despegar.p13n.hestia.api.data.model.ClosedPackagesDestinationItem;
import com.despegar.p13n.hestia.api.data.model.CruiseItem;
import com.despegar.p13n.hestia.api.data.model.CruiseRegionItem;
import com.despegar.p13n.hestia.api.data.model.FlightDestinationItem;
import com.despegar.p13n.hestia.api.data.model.HomeContent;
import com.despegar.p13n.hestia.api.data.model.HomeProduct;
import com.despegar.p13n.hestia.api.data.model.HotelDestinationItem;
import com.despegar.p13n.hestia.api.data.model.HotelItem;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.ItemType;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.PriceCarCategoryItem;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.api.data.model.RulesVersion;
import com.despegar.p13n.hestia.api.data.model.Title;
import com.despegar.p13n.hestia.api.data.model.Titles;
import com.despegar.p13n.hestia.external.geo.GeoService;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.MultiObjecHomeVersion;
import com.despegar.p13n.hestia.recommend.allinone.MultiObjectFiller;
import com.despegar.p13n.hestia.recommend.allinone.activity.UserActivity;
import com.despegar.p13n.hestia.recommend.allinone.i18n.I18nService;
import com.despegar.p13n.hestia.recommend.allinone.matrix.SearchCount;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.net.InetAddresses;

@RunWith(MockitoJUnitRunner.class)
public class MultiObjectFillerTest {

    @InjectMocks
    private MultiObjectFiller multiObjectFiller = new MultiObjectFiller();
    @Mock
    private I18nService i18nService;
    @Mock
    private GeoService geoService;
    private ActionRecommendation action;
    private HomeContent content;
    private UserRecordBuilder userProfileBuilder;
    @SuppressWarnings("unused")
    private UserActivity userActivity;
    @Mock
    private UserLocation homeUserLocation;
    @Mock
	private UserContext userContext;


    @Before
    public void setUp() {

        UserAction userAction = new UserAction();
        this.userActivity = new UserActivity(0, "MIA", "1234", 0, userAction, SearchCount.ONE_TO_THREE);

        this.userProfileBuilder = new UserRecordBuilder("1");
        Map<String, Integer> destCounter = Maps.newHashMap();
        destCounter.put("MIA", 20);
        Mockito.when(this.userContext.getSearchDestinationCounter()).thenReturn(destCounter);
        Mockito.when(this.userContext.getHistoryActionsCount()).thenReturn(55);
        Mockito.when(this.userContext.getUserRecord()).thenReturn(
            new UserRecord(this.userProfileBuilder));
        Mockito.when(this.userContext.isConfidence()).thenReturn(true);
        this.action = new ActionRecommendation("12345", Product.HOTELS, CountryCode.AR, Language.ES,
            InetAddresses.forString("123.123.123.123"), RulesVersion.DYNAMIC_SERVICE, null, null);
        this.action.setUserContext(this.userContext);

        HotelItem starred = new HotelItem("296956");


        RowHome row1 = new RowHome(null, //
            Lists.newArrayList(new CruiseItem("33124"), new CruiseItem("33129"), new CruiseRegionItem("BRA"), //
                new CruiseRegionItem("SOA")));

        Title mainTitle1 = new Title("T3");
        mainTitle1.setTitleDesc("Titulo para row1");
        Titles titles1 = new Titles(mainTitle1, null, null);
        row1.setTitles(titles1);

        RowHome row2 = new RowHome(new HotelDestinationItem("TYO"), //
            Lists.newArrayList(new FlightDestinationItem("SLA", "LAX", "ROUNDTRIP"),

            new FlightDestinationItem("SLA", "BUE", "ROUNDTRIP"), //
                new ClosedPackagesDestinationItem("ORL", "MIA"), //
                new HotelItem("12345")));

        Title mainTitle2 = new Title("T3");
        mainTitle2.setTitleDesc("Titulo para row2");
        Titles titles2 = new Titles(mainTitle2, null, null);
        row2.setTitles(titles2);


        RowHome row3 = new RowHome(new CarDestinationItem("SLA", "Airport"), //
            Lists.newArrayList(new PriceCarCategoryItem("SAO", "City", "Economic", 0, ""), //
                new CruiseItem("1234"), new CruiseItem("1236"), new ClosedPackagesDestinationItem("ORL", "MIA")));

        Title mainTitle3 = new Title("T3");
        mainTitle3.setTitleDesc("Titulo para row3");
        Titles titles3 = new Titles(mainTitle3, null, null);
        row3.setTitles(titles3);

        this.content = new HomeContent();
        Title title = new Title("T3");
        title.setTitleDesc("Hoteles");
        List<Offer> offers = Lists.newArrayList(new Offer(starred, title));

        this.content.addProduct(Product.FLIGHTS, new HomeProduct(offers, Arrays.asList(row1, row2, row3)));
        Mockito.when(this.action.getHomeUserLocation()).thenReturn(this.homeUserLocation);
        Mockito.when(this.homeUserLocation.getCity()).thenReturn("BUE");
        Mockito.when(this.geoService.normalizeToCityAirport("BUE")).thenReturn("EZE");
        Mockito.when(this.geoService.normalizeIata(Mockito.anyString())).thenReturn("RIO");
    }

    @Test
    public void testCityReviewForUS() {

        this.action.setCountryCode(CountryCode.US);
        HomeContent multiObjectHome = this.multiObjectFiller.addObjectsInRows(this.content, this.action,
            MultiObjecHomeVersion.V1_GROUPED);
        Assert.assertNotNull(multiObjectHome);
        List<RowHome> rows = multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules();
        for (RowHome rowHome : rows) {

            for (ItemHome item : rowHome.getOffers()) {
                Assert.assertNotSame(item.getId(), Product.CITY_REVIEW.name());
            }
        }
    }

    @Test
    public void testCityReviewForInt() {

        this.action.setCountryCode(CountryCode.AO);
        HomeContent multiObjectHome = this.multiObjectFiller.addObjectsInRows(this.content, this.action,
            MultiObjecHomeVersion.V1_GROUPED);
        Assert.assertNotNull(multiObjectHome);
        List<RowHome> rows = multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules();
        for (RowHome rowHome : rows) {

            for (ItemHome item : rowHome.getOffers()) {
                Assert.assertNotSame(item.getId(), Product.CITY_REVIEW.name());
            }
        }
    }

    @Test
    public void testGetMultiObjectoHomeForCorporateNo() {
        this.action.setCountryCode(CountryCode.AD);
        this.multiObjectFiller.addObjectsInOffers(this.content, this.action);
        Assert.assertTrue(this.content.getProducts().get(Product.FLIGHTS).getSpecialOffersModule().size() == 2);
    }

    @Test
    public void testGetMultiObjectoHomeForBankOfferMX() {

        this.action.setCountryCode(CountryCode.MX);
        this.multiObjectFiller.addObjectsInOffers(this.content, this.action);
        Assert.assertTrue(this.content.getProducts().get(Product.FLIGHTS).getSpecialOffersModule().get(1).getOffer().getId()
            .equals(ItemType.BANK_OFFER.name()));
    }

    @Test
    public void testGetMultiObjectoHomeForBankOfferCL() {

        this.action.setCountryCode(CountryCode.CL);
        this.multiObjectFiller.addObjectsInOffers(this.content, this.action);
        Assert.assertTrue(this.content.getProducts().get(Product.FLIGHTS).getSpecialOffersModule().get(1).getOffer().getId()
            .equals(ItemType.BANK_OFFER.name()));
    }

    @Test
    public void testGetMultiObjectoHomeForNoBankOffer() {

        this.multiObjectFiller.addObjectsInOffers(this.content, this.action);
        Assert.assertFalse(this.content.getProducts().get(Product.FLIGHTS).getSpecialOffersModule().get(0).getOffer()
            .getId().equals(ItemType.BANK_OFFER.name()));
    }

    @Test
    public void testGetMultiObjectoHomeFullRotatorSize() {

        // Mockito.when(this.userContextAccesor.getUserProfileRecord()).thenReturn(
        // new UserProfileRecord(this.userProfileBuilder));
        this.action.setCountryCode(CountryCode.AD);
        this.multiObjectFiller.addObjectsInOffers(this.content, this.action);
        Assert.assertTrue(this.content.getProducts().get(Product.FLIGHTS).getSpecialOffersModule().size() == 2);
    }

    @Test
    public void testGetMultiObjectoHomeRotatorVacationRentals() {

        HotelItem starred = new HotelItem("296956");
        Title title = new Title("T3");
        title.setTitleDesc("Hoteles");
        List<Offer> offers = Lists.newArrayList(new Offer(starred, title));
        RowHome row1 = new RowHome(null, //
            Lists.newArrayList(new CruiseItem("33124"), new CruiseItem("33129"), new CruiseRegionItem("BRA"), //
                new CruiseRegionItem("SOA")));

        Title mainTitle1 = new Title("T3");
        mainTitle1.setTitleDesc("Titulo para row1");
        Titles titles1 = new Titles(mainTitle1, null, null);
        row1.setTitles(titles1);

        RowHome row2 = new RowHome(new HotelDestinationItem("TYO"), //
            Lists.newArrayList(new FlightDestinationItem("SLA", "LAX", "ROUNDTRIP"),

            new FlightDestinationItem("SLA", "BUE", "ROUNDTRIP"), //
                new ClosedPackagesDestinationItem("ORL", "MIA"), //
                new HotelItem("12345")));

        Title mainTitle2 = new Title("T3");
        mainTitle2.setTitleDesc("Titulo para row2");
        Titles titles2 = new Titles(mainTitle2, null, null);
        row2.setTitles(titles2);


        RowHome row3 = new RowHome(new CarDestinationItem("SLA", "Airport"), //
            Lists.newArrayList(new PriceCarCategoryItem("SAO", "City", "Economic", 0, ""), //
                new CruiseItem("1234"), new CruiseItem("1236"), new ClosedPackagesDestinationItem("ORL", "MIA")));

        Title mainTitle3 = new Title("T3");
        mainTitle3.setTitleDesc("Titulo para row3");
        Titles titles3 = new Titles(mainTitle3, null, null);
        row3.setTitles(titles3);

        HomeContent content = new HomeContent();
        content.addProduct(Product.VACATIONRENTALS, new HomeProduct(offers, Arrays.asList(row1, row2, row3)));
        this.multiObjectFiller.addObjectsInOffers(content, this.action);
        Assert.assertTrue(content.getProducts().get(Product.VACATIONRENTALS).getSpecialOffersModule().size() == 2);
        Assert.assertTrue(content.getProducts().get(Product.VACATIONRENTALS).getSpecialOffersModule().get(1).getOffer()
            .getOfferType().equals(ItemType.VACATION_RENTALS_BANNER));
    }

    @Test
    public void testGetMultiObjectoHomeRotatorMinElementsSize() {

        this.userProfileBuilder.setCorporate(Corporate.NO);
        Map<String, EmailSource> emails = Maps.newHashMap();
        emails.put("sarlanga@gmail.com", EmailSource.SOCIAL);
        this.userProfileBuilder.setEmailSources(emails);
//
//        Mockito.when(this.userContextAccesor.getUserProfileRecord()).thenReturn(
//            new UserProfileRecord(this.userProfileBuilder));

        this.multiObjectFiller.addObjectsInOffers(this.content, this.action);
        Assert.assertTrue(this.content.getProducts().get(Product.FLIGHTS).getSpecialOffersModule().size() == 2);
    }

    @Test
    public void testGetMultiObjectoHomeForConfidenceUser_V1_Grouped() {

        HomeContent multiObjectHome = this.multiObjectFiller.addObjectsInRows(this.content, this.action,
            MultiObjecHomeVersion.V1_GROUPED);
        Assert.assertNotNull(multiObjectHome);
        Assert.assertTrue(multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(0).getOffers().get(4)
            .getId().equals(Product.HOLIDAYS.toString()));
        Assert.assertTrue(multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(0).getOffers().get(5)
            .getId().equals(ItemType.NEWSLETTER.name()));
        Assert.assertTrue(multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(1).getOffers().get(4)
            .getId().equals(ItemType.SECRET_OFFER.name()));
        Assert.assertTrue(multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(1).getOffers().get(5)
            .getId().equals(ItemType.CITY_REVIEW.name()));
        Assert.assertTrue(multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(2).getOffers().get(4)
            .getId().equals(Product.HAPPY_CLIENT.toString()));
    }

    @Test
    public void testGetMultiObjectoHomeForConfidenceUser_V1_INTERLIVED() {

        HomeContent multiObjectHome = this.multiObjectFiller.addObjectsInRows(this.content, this.action,
            MultiObjecHomeVersion.V1_INTERLIVED);
        Assert.assertNotNull(multiObjectHome);
        Assert.assertTrue(multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(0).getOffers().get(4)
            .getId().equals(Product.HOLIDAYS.toString()));
        Assert.assertTrue(multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(1).getOffers().get(1)
            .getId().equals(ItemType.SECRET_OFFER.name()));
        Assert.assertTrue(multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(1).getOffers().get(5)
            .getId().equals(Product.CITY_REVIEW.toString()));
        Assert.assertTrue(multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(2).getOffers().get(3)
            .getId().equals(Product.HAPPY_CLIENT.toString()));
    }

    @Test
    public void testGetMultiObjectoHomeForConfidenceUser_V2_GROUPED() {

        HomeContent multiObjectHome = this.multiObjectFiller.addObjectsInRows(this.content, this.action,
            MultiObjecHomeVersion.V2_GROUPED);
        Assert.assertNotNull(multiObjectHome);
        Assert.assertTrue(multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(0).getOffers().get(4)
            .getId().equals(Product.HAPPY_CLIENT.toString()));
        Assert.assertTrue(multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(1).getOffers().get(4)
            .getId().equals(Product.HAPPY_CLIENT.toString()));
        Assert.assertTrue(multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(2).getOffers().get(4)
            .getId().equals(Product.HAPPY_CLIENT.toString()));
    }

    @Test
    public void testGetMultiObjectoHomeForConfidenceUser_V2_INTERLIVED() {

        HomeContent multiObjectHome = this.multiObjectFiller.addObjectsInRows(this.content, this.action,
            MultiObjecHomeVersion.V2_INTERLIVED);
        Assert.assertNotNull(multiObjectHome);
        Assert.assertTrue(multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(0).getOffers().get(2)
            .getId().equals(Product.HAPPY_CLIENT.toString()));
        Assert.assertTrue(multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(1).getOffers().get(5)
            .getId().equals(Product.HAPPY_CLIENT.toString()));
        Assert.assertTrue(multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(2).getOffers().get(3)
            .getId().equals(Product.HAPPY_CLIENT.toString()));
    }

    @Test
    public void testGetMultiObjectoHomeForConfidenceUser_V3_GROUPED() {

        HomeContent multiObjectHome = this.multiObjectFiller.addObjectsInRows(this.content, this.action,
            MultiObjecHomeVersion.V3_GROUPED);
        Assert.assertNotNull(multiObjectHome);
        Assert.assertTrue(multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(0).getOffers().get(4)
            .getId().equals(Product.HAPPY_CLIENT.toString()));
        Assert.assertTrue(multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(0).getOffers().get(5)
            .getId().equals(Product.HAPPY_CLIENT.toString()));
        Assert.assertTrue(multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(1).getOffers().get(4)
            .getId().equals(Product.HAPPY_CLIENT.toString()));
        for (ItemHome item : multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(2).getOffers()) {
            Assert.assertFalse(item.getId().equals(Product.HAPPY_CLIENT.toString()));
        }
    }

    @Test
    public void testGetMultiObjectoHomeForConfidenceUser_V3_INTERLIVED() {

        HomeContent multiObjectHome = this.multiObjectFiller.addObjectsInRows(this.content, this.action,
            MultiObjecHomeVersion.V3_INTERLIVED);
        Assert.assertNotNull(multiObjectHome);
        Assert.assertTrue(multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(0).getOffers().get(0)
            .getId().equals(Product.HAPPY_CLIENT.toString()));
        Assert.assertTrue(multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(0).getOffers().get(4)
            .getId().equals(Product.HAPPY_CLIENT.toString()));
        Assert.assertTrue(multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(1).getOffers().get(4)
            .getId().equals(Product.HAPPY_CLIENT.toString()));
        for (ItemHome item : multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(2).getOffers()) {
            Assert.assertFalse(item.getId().equals(Product.HAPPY_CLIENT.toString()));
        }
    }

    @Test
    public void testGetMultiObjectoHomeForTRUST_PILOT_ALERT_V1_Grouped() {

        HomeContent multiObjectHome = this.multiObjectFiller.addObjectsInRows(this.content, this.action,
            MultiObjecHomeVersion.V1_GROUPED);
        Assert.assertNotNull(multiObjectHome);
        Assert.assertTrue(multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(0).getOffers().get(5)
            .getId().equals(ItemType.NEWSLETTER.name()));
        Assert.assertTrue(multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(0).getOffers().get(4)
            .getId().equals(ItemType.HOLIDAYS.name()));
        Assert.assertTrue(multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(1).getOffers().get(4)
            .getId().equals(ItemType.SECRET_OFFER.name()));
        Assert.assertTrue(multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(1).getOffers().get(5)
            .getId().equals(ItemType.CITY_REVIEW.name()));
        Assert.assertTrue(multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(2).getOffers().get(5)
            .getId().equals(ItemType.TRUST_PILOT.name()));

        for (ItemHome item : multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(1).getOffers()) {
            Assert.assertFalse(item.getId().equals(ItemType.FARE_ALERTS.name()));
            Assert.assertFalse(item.getId().equals(ItemType.TRUST_PILOT.name()));
        }
    }

    @Test
    public void testGetMultiObjectoHomeForTRUST_PILOT_ALERT_V1_Interlived() {

        HomeContent multiObjectHome = this.multiObjectFiller.addObjectsInRows(this.content, this.action,
            MultiObjecHomeVersion.V1_INTERLIVED);
        Assert.assertNotNull(multiObjectHome);
        Assert.assertTrue(multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(0).getOffers().get(0)
            .getId().equals(ItemType.NEWSLETTER.name()));
        Assert.assertTrue(multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(0).getOffers().get(4)
            .getId().equals(ItemType.HOLIDAYS.name()));
        Assert.assertTrue(multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(1).getOffers().get(1)
            .getId().equals(ItemType.SECRET_OFFER.name()));
        Assert.assertTrue(multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(1).getOffers().get(5)
            .getId().equals(ItemType.CITY_REVIEW.name()));
        Assert.assertTrue(multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(2).getOffers().get(0)
            .getId().equals(ItemType.TRUST_PILOT.name()));
        for (ItemHome item : multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(1).getOffers()) {
            Assert.assertFalse(item.getId().equals(ItemType.FARE_ALERTS.name()));
            Assert.assertFalse(item.getId().equals(ItemType.TRUST_PILOT.name()));
        }
    }

    @Test
    public void testGetMultiObjectoHomeForTRUST_PILOT_ALERT_V2_Grouped() {

        HomeContent multiObjectHome = this.multiObjectFiller.addObjectsInRows(this.content, this.action,
            MultiObjecHomeVersion.V2_GROUPED);
        Assert.assertNotNull(multiObjectHome);
        Assert.assertTrue(multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(1).getOffers().get(5)
            .getId().equals(ItemType.FARE_ALERTS.name()));
        Assert.assertTrue(multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(2).getOffers().get(5)
            .getId().equals(ItemType.TRUST_PILOT.name()));
        for (ItemHome item : multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(0).getOffers()) {
            Assert.assertFalse(item.getId().equals(ItemType.FARE_ALERTS.name()));
            Assert.assertFalse(item.getId().equals(ItemType.TRUST_PILOT.name()));
        }
    }

    @Test
    public void testGetMultiObjectoHomeForTRUST_PILOT_ALERT_V2_interlived() {

        HomeContent multiObjectHome = this.multiObjectFiller.addObjectsInRows(this.content, this.action,
            MultiObjecHomeVersion.V2_INTERLIVED);
        Assert.assertNotNull(multiObjectHome);
        Assert.assertTrue(multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(1).getOffers().get(1)
            .getId().equals(ItemType.FARE_ALERTS.name()));
        Assert.assertTrue(multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(2).getOffers().get(0)
            .getId().equals(ItemType.TRUST_PILOT.name()));
        for (ItemHome item : multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(0).getOffers()) {
            Assert.assertFalse(item.getId().equals(ItemType.FARE_ALERTS.name()));
            Assert.assertFalse(item.getId().equals(ItemType.TRUST_PILOT.name()));
        }
    }

    @Test
    public void testGetMultiObjectoHomeForTRUST_PILOT_ALERT_V3_grouped() {

        HomeContent multiObjectHome = this.multiObjectFiller.addObjectsInRows(this.content, this.action,
            MultiObjecHomeVersion.V3_GROUPED);
        Assert.assertNotNull(multiObjectHome);
        Assert.assertTrue(multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(2).getOffers().get(5)
            .getId().equals(ItemType.FARE_ALERTS.name()));
        Assert.assertTrue(multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(2).getOffers().get(4)
            .getId().equals(ItemType.TRUST_PILOT.name()));
        for (ItemHome item : multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(0).getOffers()) {
            Assert.assertFalse(item.getId().equals(ItemType.FARE_ALERTS.name()));
            Assert.assertFalse(item.getId().equals(ItemType.TRUST_PILOT.name()));
        }
        for (ItemHome item : multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(1).getOffers()) {
            Assert.assertFalse(item.getId().equals(ItemType.FARE_ALERTS.name()));
            Assert.assertFalse(item.getId().equals(ItemType.TRUST_PILOT.name()));
        }
    }

    @Test
    public void testGetMultiObjectoHomeForTRUST_PILOT_ALERT_V3_interlived() {

        HomeContent multiObjectHome = this.multiObjectFiller.addObjectsInRows(this.content, this.action,
            MultiObjecHomeVersion.V3_INTERLIVED);
        Assert.assertNotNull(multiObjectHome);
        Assert.assertTrue(multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(2).getOffers().get(5)
            .getId().equals(ItemType.FARE_ALERTS.name()));
        Assert.assertTrue(multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(2).getOffers().get(0)
            .getId().equals(ItemType.TRUST_PILOT.name()));
        for (ItemHome item : multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(0).getOffers()) {
            Assert.assertFalse(item.getId().equals(ItemType.FARE_ALERTS.name()));
            Assert.assertFalse(item.getId().equals(ItemType.TRUST_PILOT.name()));
        }
        for (ItemHome item : multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(1).getOffers()) {
            Assert.assertFalse(item.getId().equals(ItemType.FARE_ALERTS.name()));
            Assert.assertFalse(item.getId().equals(ItemType.TRUST_PILOT.name()));
        }
    }


    @Test
    public void testGetMultiObjectoHomeForNoCityReview() {

        this.multiObjectFiller.setSearchDestinationDemand(222);
        HomeContent multiObjectHome = this.multiObjectFiller.addObjectsInRows(this.content, this.action,
            MultiObjecHomeVersion.V3_INTERLIVED);
        Assert.assertNotNull(multiObjectHome);
        for (ItemHome item : multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(0).getOffers()) {
            Assert.assertFalse(item.getId().equals(Product.CITY_REVIEW.toString()));
        }
        for (ItemHome item : multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(1).getOffers()) {
            Assert.assertFalse(item.getId().equals(Product.CITY_REVIEW.toString()));
        }
        for (ItemHome item : multiObjectHome.getProducts().get(Product.FLIGHTS).getRowModules().get(2).getOffers()) {
            Assert.assertFalse(item.getId().equals(Product.CITY_REVIEW.toString()));
        }
    }
}
