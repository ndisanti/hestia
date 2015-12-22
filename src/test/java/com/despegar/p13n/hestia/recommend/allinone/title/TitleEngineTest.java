package com.despegar.p13n.hestia.recommend.allinone.title;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import ar.com.despegar.p13n.hestia.TestFuntions;
import ar.com.despegar.p13n.hestia.test.MockitoAnnotationBaseTest;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Language;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.UserContext;
import com.despegar.p13n.euler.commons.client.model.UserRecord;
import com.despegar.p13n.hestia.api.data.model.ActivityDestinationItem;
import com.despegar.p13n.hestia.api.data.model.ActivityItem;
import com.despegar.p13n.hestia.api.data.model.CarDestinationItem;
import com.despegar.p13n.hestia.api.data.model.ClosedPackagesDestinationItem;
import com.despegar.p13n.hestia.api.data.model.ClosedPackagesItem;
import com.despegar.p13n.hestia.api.data.model.CruiseItem;
import com.despegar.p13n.hestia.api.data.model.FlightDestinationItem;
import com.despegar.p13n.hestia.api.data.model.HotelDestinationItem;
import com.despegar.p13n.hestia.api.data.model.HotelItem;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.ItemType;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.api.data.model.RulesVersion;
import com.despegar.p13n.hestia.external.geo.GeoService;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.activity.BuyActivity;
import com.despegar.p13n.hestia.recommend.allinone.activity.UserActivity;
import com.despegar.p13n.hestia.recommend.allinone.i18n.I18nService;
import com.despegar.p13n.hestia.recommend.allinone.matrix.SearchCount;
import com.despegar.p13n.hestia.recommend.allinone.rules.RuleSection;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Function;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;

@RunWith(MockitoJUnitRunner.class)
public class TitleEngineTest
    extends MockitoAnnotationBaseTest {

    @Mock
    private I18nService i18nService;

    @Mock
    private GeoService geoService;

    private TitleEngine titleEngine = new TitleEngine();

    @Mock
    private UserContext userContextAccesor;
    @Mock
    private UserRecord userPrifileRecord;

    @Before
    public void before() {

        this.titleEngine.setI18nService(this.i18nService);
        Mockito.when(this.geoService.normalizeIata(Matchers.anyString())).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                return (String) args[0];
            }
        });
        Mockito.when(this.userContextAccesor.getUserRecord()).thenReturn(this.userPrifileRecord);
        Mockito.when(this.userPrifileRecord.getLastIntenseSearch()).thenReturn(null);
        this.titleEngine.setGeoService(this.geoService);

    }

    @Test
    public void testOfferDynamic() {

        final ActionRecommendation action = new ActionRecommendation("uuid", Product.FLIGHTS, CountryCode.AR, Language.ES,
            null, RulesVersion.DYNAMIC_SERVICE, null, null);
        action.setUserContext(this.userContextAccesor);
        TitleData td = new TitleData(TitleEnum.T19, Product.HOTELS, null, "MIA");
        action.setTitleData(td);

        Mockito.when(this.i18nService.getI18nTitle(td, Language.ES, CountryCode.AR)).thenReturn(
            "Paquetes Turísticos Nacionales");


        UserActivity activity = new UserActivity(5, "MIA", "123", 5, null, SearchCount.NA);
        action.setBuyActivity(new BuyActivity(Product.FLIGHTS, activity));

        RuleSection section = new RuleSection(Product.HOTELS, Function.BUY_DESTINATION);

        Offer offer = new Offer(new HotelDestinationItem("MIA"), null);

        this.titleEngine.updateTitle(action, section, offer);
        boolean equals = false;
        for (TitleEnum title : TitleListEnum.L1.getTitles()) {
            if (title.name() == offer.getTitleOffer().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
    }

    @Test
    public void testUpdateTitleOfferMultiProductOfferBuy() {

        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.HOTELS, "MIA");
        action.getTitleData().addDestination("MIA");
        action.setUserContext(this.userContextAccesor);
        Offer offer = new Offer(new HotelDestinationItem("MIA"), null);
        this.titleEngine.updateTitle(action, offer);

        boolean equals = false;
        for (TitleEnum title : TitleListEnum.L1.getTitles()) {
            if (title.name() == offer.getTitleOffer().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
        equals = false;
        offer = new Offer(new HotelItem("12345"), null);
        this.titleEngine.updateTitle(action, offer);
        for (TitleEnum title : TitleListEnum.L1.getTitles()) {
            if (title.name() == offer.getTitleOffer().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
        offer = new Offer(new CarDestinationItem("RIO", "sss"), null);
        this.titleEngine.updateTitle(action, offer);
        equals = false;
        for (TitleEnum title : TitleListEnum.L1.getTitles()) {
            if (title.name() == offer.getTitleOffer().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
        offer = new Offer(new ClosedPackagesItem("CLUID"), null);
        this.titleEngine.updateTitle(action, offer);
        equals = false;
        for (TitleEnum title : TitleListEnum.L3.getTitles()) {
            if (title.name() == offer.getTitleOffer().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
        offer = new Offer(new ClosedPackagesDestinationItem("RIO", "MDP"), null);
        this.titleEngine.updateTitle(action, offer);
        equals = false;
        for (TitleEnum title : TitleListEnum.L3.getTitles()) {
            if (title.name() == offer.getTitleOffer().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
        offer = new Offer(new CruiseItem("DID11"), null);
        this.titleEngine.updateTitle(action, offer);
        equals = false;
        for (TitleEnum title : TitleListEnum.L1.getTitles()) {
            if (title.name() == offer.getTitleOffer().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
        offer = new Offer(new FlightDestinationItem("BUE", "MDP", "LLL"), null);
        this.titleEngine.updateTitle(action, offer);
        equals = false;
        for (TitleEnum title : TitleListEnum.L4.getTitles()) {
            if (title.name() == offer.getTitleOffer().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
        offer = new Offer(new ActivityItem("ACTID11"), null);
        this.titleEngine.updateTitle(action, offer);
        equals = false;
        for (TitleEnum title : TitleListEnum.L1.getTitles()) {
            if (title.name() == offer.getTitleOffer().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
    }

    @Test
    public void testUpdateTitleOfferMultiProductOfferSearch() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Search(Product.HOTELS, "MIA");
        Offer offer = new Offer(new HotelDestinationItem("MIA"), null);
        action.getTitleData().addDestination("MIA");
        this.titleEngine.updateTitle(action, offer);

        boolean equals = false;
        for (TitleEnum title : TitleListEnum.L1.getTitles()) {
            if (title.name() == offer.getTitleOffer().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
        offer = new Offer(new HotelItem("12345"), null);
        this.titleEngine.updateTitle(action, offer);
        equals = false;
        for (TitleEnum title : TitleListEnum.L1.getTitles()) {
            if (title.name() == offer.getTitleOffer().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
        offer = new Offer(new CarDestinationItem("RIO", "sss"), null);
        this.titleEngine.updateTitle(action, offer);
        equals = false;
        for (TitleEnum title : TitleListEnum.L1.getTitles()) {
            if (title.name() == offer.getTitleOffer().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
        offer = new Offer(new ClosedPackagesItem("CLUID"), null);
        this.titleEngine.updateTitle(action, offer);
        equals = false;
        for (TitleEnum title : TitleListEnum.L1.getTitles()) {
            if (title.name() == offer.getTitleOffer().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
        offer = new Offer(new ClosedPackagesDestinationItem("RIO", "MDP"), null);
        this.titleEngine.updateTitle(action, offer);
        equals = false;
        for (TitleEnum title : TitleListEnum.L1.getTitles()) {
            if (title.name() == offer.getTitleOffer().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
        offer = new Offer(new CruiseItem("DID11"), null);
        this.titleEngine.updateTitle(action, offer);
        equals = false;
        for (TitleEnum title : TitleListEnum.L1.getTitles()) {
            if (title.name() == offer.getTitleOffer().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
        offer = new Offer(new FlightDestinationItem("BUE", "MDP", "LLL"), null);
        this.titleEngine.updateTitle(action, offer);
        equals = false;
        for (TitleEnum title : TitleListEnum.L4.getTitles()) {
            if (title.name() == offer.getTitleOffer().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
        offer = new Offer(new ActivityItem("ACTID11"), null);
        this.titleEngine.updateTitle(action, offer);
        equals = false;
        for (TitleEnum title : TitleListEnum.L1.getTitles()) {
            if (title.name() == offer.getTitleOffer().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
    }

    @Test
    public void testUpdateTitleOfferMultiProductRowBuy() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.HOTELS, "MIA");
        action.setUserContext(this.userContextAccesor);
        action.setCurrentHome(Product.CARS);
        List<ItemHome> offers = new ArrayList<ItemHome>();
        offers.add(new CarDestinationItem("MIA", "asdas"));
        offers.add(new CarDestinationItem("BUE", "asdas"));
        offers.add(new CarDestinationItem("RIO", "asdas"));
        action.getTitleData().addDestination("MIA");
        RowHome rowHome = new RowHome(null, offers);
        this.titleEngine.updateTitle(action, rowHome);
        boolean equals = false;
        for (TitleEnum title : TitleListEnum.L1.getTitles()) {
            if (title.name() == rowHome.getTitles().getMainTitle().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
        offers = new ArrayList<ItemHome>();
        offers.add(new CarDestinationItem("MIA", "asdas"));
        offers.add(new FlightDestinationItem("MIA", "BUE", "asdas"));
        offers.add(new CarDestinationItem("RIO", "asdas"));
        rowHome = new RowHome(null, offers);
        this.titleEngine.updateTitle(action, rowHome);
        equals = false;
        for (TitleEnum title : TitleListEnum.L53.getTitles()) {
            if (title.name() == rowHome.getTitles().getMainTitle().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
        offers = new ArrayList<ItemHome>();
        offers.add(new FlightDestinationItem("BUE", "RIO", "asdasd"));
        offers.add(new FlightDestinationItem("MIA", "BUE", "asdas"));
        rowHome = new RowHome(null, offers);
        this.titleEngine.updateTitle(action, rowHome);
        equals = false;
        for (TitleEnum title : TitleListEnum.L4.getTitles()) {
            if (title.name() == rowHome.getTitles().getMainTitle().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
        offers = new ArrayList<ItemHome>();
        offers.add(new HotelDestinationItem("MDP"));
        offers.add(new HotelItem("12345"));
        rowHome = new RowHome(null, offers);
        this.titleEngine.updateTitle(action, rowHome);
        equals = false;
        for (TitleEnum title : TitleListEnum.L1.getTitles()) {
            if (title.name() == rowHome.getTitles().getMainTitle().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
        offers = new ArrayList<ItemHome>();
        offers.add(new ClosedPackagesDestinationItem("MDP", "MIA"));
        offers.add(new ClosedPackagesItem("CLUID123"));
        rowHome = new RowHome(null, offers);
        this.titleEngine.updateTitle(action, rowHome);
        equals = false;
        for (TitleEnum title : TitleListEnum.L4.getTitles()) {
            if (title.name() == rowHome.getTitles().getMainTitle().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
        offers = new ArrayList<ItemHome>();
        offers.add(new ActivityDestinationItem("MDQ"));
        offers.add(new ActivityItem("ACTID123"));
        rowHome = new RowHome(null, offers);
        this.titleEngine.updateTitle(action, rowHome);
        equals = false;
        for (TitleEnum title : TitleListEnum.L3.getTitles()) {
            if (title.name() == rowHome.getTitles().getMainTitle().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
        offers = new ArrayList<ItemHome>();
        offers.add(new CruiseItem("ID12345"));
        offers.add(new CruiseItem("ID12347"));
        rowHome = new RowHome(null, offers);
        this.titleEngine.updateTitle(action, rowHome);
        equals = false;
        for (TitleEnum title : TitleListEnum.L3.getTitles()) {
            if (title.name() == rowHome.getTitles().getMainTitle().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
    }


    @Test
    public void testUpdateTitleOfferMultiProductRowSearch() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Search(Product.HOTELS, "MIA");
        action.setCurrentHome(Product.CARS);
        List<ItemHome> offers = new ArrayList<ItemHome>();
        offers.add(new CarDestinationItem("MIA", "asdas"));
        offers.add(new CarDestinationItem("BUE", "asdas"));
        offers.add(new CarDestinationItem("RIO", "asdas"));
        action.getTitleData().addDestination("MIA");
        RowHome rowHome = new RowHome(null, offers);
        this.titleEngine.updateTitle(action, rowHome);
        boolean equals = false;
        for (TitleEnum title : TitleListEnum.L1.getTitles()) {
            if (title.name() == rowHome.getTitles().getMainTitle().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
        offers = new ArrayList<ItemHome>();
        offers.add(new CarDestinationItem("MIA", "asdas"));
        offers.add(new FlightDestinationItem("MIA", "BUE", "asdas"));
        offers.add(new CarDestinationItem("RIO", "asdas"));
        rowHome = new RowHome(null, offers);
        this.titleEngine.updateTitle(action, rowHome);
        equals = false;
        for (TitleEnum title : TitleListEnum.L52.getTitles()) {
            if (title.name() == rowHome.getTitles().getMainTitle().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
        offers = new ArrayList<ItemHome>();
        offers.add(new FlightDestinationItem("BUE", "RIO", "asdasd"));
        offers.add(new FlightDestinationItem("MIA", "BUE", "asdas"));
        rowHome = new RowHome(null, offers);
        this.titleEngine.updateTitle(action, rowHome);
        equals = false;
        for (TitleEnum title : TitleListEnum.L4.getTitles()) {
            if (title.name() == rowHome.getTitles().getMainTitle().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
        offers = new ArrayList<ItemHome>();
        offers.add(new HotelDestinationItem("MDP"));
        offers.add(new HotelItem("12345"));
        rowHome = new RowHome(null, offers);
        this.titleEngine.updateTitle(action, rowHome);
        equals = false;
        for (TitleEnum title : TitleListEnum.L1.getTitles()) {
            if (title.name() == rowHome.getTitles().getMainTitle().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
        offers = new ArrayList<ItemHome>();
        offers.add(new ClosedPackagesDestinationItem("MDP", "MIA"));
        offers.add(new ClosedPackagesItem("CLUID123"));
        rowHome = new RowHome(null, offers);
        this.titleEngine.updateTitle(action, rowHome);
        equals = false;
        for (TitleEnum title : TitleListEnum.L4.getTitles()) {
            if (title.name() == rowHome.getTitles().getMainTitle().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
        offers = new ArrayList<ItemHome>();
        offers.add(new ActivityDestinationItem("MDQ"));
        offers.add(new ActivityItem("ACTID123"));
        rowHome = new RowHome(null, offers);
        this.titleEngine.updateTitle(action, rowHome);
        equals = false;
        for (TitleEnum title : TitleListEnum.L3.getTitles()) {
            if (title.name() == rowHome.getTitles().getMainTitle().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
        offers = new ArrayList<ItemHome>();
        offers.add(new CruiseItem("ID12345"));
        offers.add(new CruiseItem("ID12347"));
        rowHome = new RowHome(null, offers);
        this.titleEngine.updateTitle(action, rowHome);
        equals = false;
        for (TitleEnum title : TitleListEnum.L3.getTitles()) {
            if (title.name() == rowHome.getTitles().getMainTitle().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
    }

    @Test
    public void testUpdateTitleOfferMultiProductMultiDestinationRowBuy() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.HOTELS, "MIA");
        action.setCurrentHome(Product.CARS);
        action.getTitleData().addDestination("MIA");
        action.getTitleData().addDestination("BUE");
        action.setOrigin("RIO");
        action.setUserContext(this.userContextAccesor);
        List<ItemHome> offers = new ArrayList<ItemHome>();
        offers.add(new CarDestinationItem("MIA", "asdas"));
        offers.add(new CarDestinationItem("BUE", "asdas"));
        offers.add(new CarDestinationItem("RIO", "asdas"));
        action.getTitleData().addDestination("MIA");
        RowHome rowHome = new RowHome(null, offers);
        this.titleEngine.updateTitle(action, rowHome);
        boolean equals = false;
        for (TitleEnum title : TitleListEnum.L3.getTitles()) {
            if (title.name() == rowHome.getTitles().getMainTitle().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
        offers = new ArrayList<ItemHome>();
        offers.add(new CarDestinationItem("MIA", "asdas"));
        offers.add(new FlightDestinationItem("MIA", "BUE", "asdas"));
        offers.add(new CarDestinationItem("RIO", "asdas"));
        rowHome = new RowHome(null, offers);
        this.titleEngine.updateTitle(action, rowHome);
        equals = false;
        for (TitleEnum title : TitleListEnum.L55.getTitles()) {
            if (title.name() == rowHome.getTitles().getMainTitle().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
        offers = new ArrayList<ItemHome>();
        offers.add(new FlightDestinationItem("BUE", "RIO", "asdasd"));
        offers.add(new FlightDestinationItem("MIA", "BUE", "asdas"));
        rowHome = new RowHome(null, offers);
        this.titleEngine.updateTitle(action, rowHome);
        equals = false;
        for (TitleEnum title : TitleListEnum.L6.getTitles()) {
            if (title.name() == rowHome.getTitles().getMainTitle().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
        offers = new ArrayList<ItemHome>();
        offers.add(new HotelDestinationItem("MDP"));
        offers.add(new HotelItem("12345"));
        rowHome = new RowHome(null, offers);
        this.titleEngine.updateTitle(action, rowHome);
        equals = false;
        for (TitleEnum title : TitleListEnum.L3.getTitles()) {
            if (title.name() == rowHome.getTitles().getMainTitle().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
        offers = new ArrayList<ItemHome>();
        offers.add(new ClosedPackagesDestinationItem("MDP", "MIA"));
        offers.add(new ClosedPackagesItem("CLUID123"));
        rowHome = new RowHome(null, offers);
        this.titleEngine.updateTitle(action, rowHome);
        equals = false;
        for (TitleEnum title : TitleListEnum.L3.getTitles()) {
            if (title.name() == rowHome.getTitles().getMainTitle().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
        offers = new ArrayList<ItemHome>();
        offers.add(new ActivityDestinationItem("MDQ"));
        offers.add(new ActivityItem("ACTID123"));
        rowHome = new RowHome(null, offers);
        this.titleEngine.updateTitle(action, rowHome);
        equals = false;
        for (TitleEnum title : TitleListEnum.L24.getTitles()) {
            if (title.name() == rowHome.getTitles().getMainTitle().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
        offers = new ArrayList<ItemHome>();
        offers.add(new CruiseItem("ID12345"));
        offers.add(new CruiseItem("ID12347"));
        rowHome = new RowHome(null, offers);
        this.titleEngine.updateTitle(action, rowHome);
        equals = false;
        for (TitleEnum title : TitleListEnum.L24.getTitles()) {
            if (title.name() == rowHome.getTitles().getMainTitle().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
    }

    @Test
    public void testUpdateTitleOfferMultiProductMultiDestinationRowSearch() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Search(Product.HOTELS, "MIA");
        action.setCurrentHome(Product.CARS);
        action.getTitleData().addDestination("MIA");
        action.getTitleData().addDestination("BUE");
        List<ItemHome> offers = new ArrayList<ItemHome>();
        offers.add(new CarDestinationItem("MIA", "asdas"));
        offers.add(new CarDestinationItem("BUE", "asdas"));
        offers.add(new CarDestinationItem("RIO", "asdas"));
        action.getTitleData().addDestination("MIA");
        RowHome rowHome = new RowHome(null, offers);
        this.titleEngine.updateTitle(action, rowHome);
        boolean equals = false;
        for (TitleEnum title : TitleListEnum.L3.getTitles()) {
            if (title.name() == rowHome.getTitles().getMainTitle().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
        offers = new ArrayList<ItemHome>();
        offers.add(new CarDestinationItem("MIA", "asdas"));
        offers.add(new FlightDestinationItem("MIA", "BUE", "asdas"));
        offers.add(new CarDestinationItem("RIO", "asdas"));
        rowHome = new RowHome(null, offers);
        this.titleEngine.updateTitle(action, rowHome);
        equals = false;
        for (TitleEnum title : TitleListEnum.L54.getTitles()) {
            if (title.name() == rowHome.getTitles().getMainTitle().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
        offers = new ArrayList<ItemHome>();
        offers.add(new FlightDestinationItem("BUE", "RIO", "asdasd"));
        offers.add(new FlightDestinationItem("MIA", "BUE", "asdas"));
        rowHome = new RowHome(null, offers);
        this.titleEngine.updateTitle(action, rowHome);
        equals = false;
        for (TitleEnum title : TitleListEnum.L3.getTitles()) {
            if (title.name() == rowHome.getTitles().getMainTitle().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
        offers = new ArrayList<ItemHome>();
        offers.add(new HotelDestinationItem("MDP"));
        offers.add(new HotelItem("12345"));
        rowHome = new RowHome(null, offers);
        this.titleEngine.updateTitle(action, rowHome);
        equals = false;
        for (TitleEnum title : TitleListEnum.L3.getTitles()) {
            if (title.name() == rowHome.getTitles().getMainTitle().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
        offers = new ArrayList<ItemHome>();
        offers.add(new ClosedPackagesDestinationItem("MDP", "MIA"));
        offers.add(new ClosedPackagesItem("CLUID123"));
        rowHome = new RowHome(null, offers);
        this.titleEngine.updateTitle(action, rowHome);
        equals = false;
        for (TitleEnum title : TitleListEnum.L3.getTitles()) {
            if (title.name() == rowHome.getTitles().getMainTitle().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
        offers = new ArrayList<ItemHome>();
        offers.add(new ActivityDestinationItem("MDQ"));
        offers.add(new ActivityItem("ACTID123"));
        rowHome = new RowHome(null, offers);
        this.titleEngine.updateTitle(action, rowHome);
        equals = false;
        for (TitleEnum title : TitleListEnum.L24.getTitles()) {
            if (title.name() == rowHome.getTitles().getMainTitle().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
        offers = new ArrayList<ItemHome>();
        offers.add(new CruiseItem("ID12345"));
        offers.add(new CruiseItem("ID12347"));
        rowHome = new RowHome(null, offers);
        this.titleEngine.updateTitle(action, rowHome);
        equals = false;
        for (TitleEnum title : TitleListEnum.L24.getTitles()) {
            if (title.name() == rowHome.getTitles().getMainTitle().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);
    }



    @Test
    public void testRowDynamic() {

        this.titleEngine.add(new MonoProductTitleKey(ActivityType.BUY, //
            SectionType.ROW,//
            SectionFunctionCode.BUY_DESTINATION,//
            ItemType.HOTEL_DESTINATION), //
            TitleListEnum.L1);

        final ActionRecommendation action = new ActionRecommendation("uuid", Product.FLIGHTS, CountryCode.BR, Language.ES,
            null, RulesVersion.DYNAMIC_SERVICE, null, null);
        action.setTitleData(new TitleData(TitleEnum.T19, Product.HOTELS, null, "MIA"));
        action.setUserContext(this.userContextAccesor);

        UserActivity activity = new UserActivity(5, "MIA", "123", 5, null, SearchCount.NA);
        action.setBuyActivity(new BuyActivity(Product.FLIGHTS, activity));

        RuleSection section = new RuleSection(Product.HOTELS, Function.BUY_DESTINATION);

        List<HotelDestinationItem> offers = new ArrayList<HotelDestinationItem>();
        offers.add(new HotelDestinationItem("MIA"));
        RowHome rowHome = new RowHome(null, offers);

        action.setCurrentHome(Product.HOTELS);

        this.titleEngine.updateTitle(action, section, rowHome);
        boolean equals = false;
        for (TitleEnum title : TitleListEnum.L1.getTitles()) {
            if (title.name() == rowHome.getTitles().getMainTitle().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);

        this.titleEngine.updateTitle(action, section, rowHome);
        equals = false;
        for (TitleEnum title : TitleListEnum.L1.getTitles()) {
            if (title.name() == rowHome.getTitles().getMainTitle().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);

        this.titleEngine.updateTitle(action, section, rowHome);
        equals = false;
        for (TitleEnum title : TitleListEnum.L1.getTitles()) {
            if (title.name() == rowHome.getTitles().getMainTitle().getTitleId()) {
                equals = true;
            }
        }
        Assert.assertTrue(equals);


    }

    @Test
    public void testMissing() {

        final ActionRecommendation action = new ActionRecommendation("uuid", Product.FLIGHTS, CountryCode.AR, Language.ES,
            null, RulesVersion.DYNAMIC_SERVICE, null, null);
        action.setTitleData(new TitleData(TitleEnum.T19, Product.HOTELS, null, "MIA"));

        RuleSection section = new RuleSection(Product.HOTELS, Function.BUY_DESTINATION);

        List<HotelDestinationItem> offers = new ArrayList<HotelDestinationItem>();
        offers.add(new HotelDestinationItem("MIA"));

        RowHome rowHome = new RowHome(null, offers);

        action.setCurrentHome(Product.HOTELS);

        this.titleEngine.updateTitle(action, section, rowHome);

        Assert.assertFalse(this.titleEngine.dumpMissing().isEmpty());

    }

    @Test
    public void testDuplicated() {
        new TitleEngine().init();
    }

}
