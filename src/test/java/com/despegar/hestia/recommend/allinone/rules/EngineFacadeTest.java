/**
 * 
 */
package com.despegar.hestia.recommend.allinone.rules;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
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

import ar.com.despegar.p13n.hestia.MockitoAnnotationBaseTest;
import ar.com.despegar.p13n.hestia.TestFuntions;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Language;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.UserContext;
import com.despegar.p13n.euler.commons.client.model.UserRecord;
import com.despegar.p13n.hestia.api.data.model.HomeContent;
import com.despegar.p13n.hestia.api.data.model.HomeProduct;
import com.despegar.p13n.hestia.api.data.model.HotelDestinationItem;
import com.despegar.p13n.hestia.api.data.model.HotelItem;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.api.data.model.RulesVersion;
import com.despegar.p13n.hestia.external.geo.GeoService;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.activity.Anticipation;
import com.despegar.p13n.hestia.recommend.allinone.activity.LastAction;
import com.despegar.p13n.hestia.recommend.allinone.rules.BuyProductSupport;
import com.despegar.p13n.hestia.recommend.allinone.rules.CountrySupport;
import com.despegar.p13n.hestia.recommend.allinone.rules.EngineFacade;
import com.despegar.p13n.hestia.recommend.allinone.rules.HomeContentValidator;
import com.despegar.p13n.hestia.recommend.allinone.rules.HomeSupport;
import com.despegar.p13n.hestia.recommend.allinone.rules.LastResortEngine;
import com.despegar.p13n.hestia.recommend.allinone.rules.NoHistoryEngine;
import com.despegar.p13n.hestia.recommend.allinone.rules.RuleContentFiller;
import com.despegar.p13n.hestia.recommend.allinone.rules.RuleContentPriority;
import com.despegar.p13n.hestia.recommend.allinone.rules.RuleEngine;
import com.despegar.p13n.hestia.recommend.allinone.rules.RuleSection;
import com.despegar.p13n.hestia.recommend.allinone.rules.SectionFunctionEngine;
import com.despegar.p13n.hestia.recommend.allinone.rules.SectionRuleEngine;
import com.despegar.p13n.hestia.recommend.allinone.rules.builder.SectionRuleBuilder;
import com.despegar.p13n.hestia.recommend.allinone.rules.item.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.BaseSectionFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Function;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionWrapper;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent.RuleDefSection;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent.SectionRuleContent;
import com.despegar.p13n.hestia.recommend.allinone.title.TitleEngine;
import com.google.common.collect.Lists;
import com.google.common.net.InetAddresses;


/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class EngineFacadeTest
    extends MockitoAnnotationBaseTest {

    public static final HotelDestinationItem OFFER = new HotelDestinationItem("s");
    public static final HotelDestinationItem HIGHTLIGHTED = new HotelDestinationItem("h");
    public static final HotelDestinationItem CELL1 = new HotelDestinationItem("c1");
    public static final HotelDestinationItem CELL2 = new HotelDestinationItem("c2");
    public static final HotelDestinationItem CELL3 = new HotelDestinationItem("c3");

    @Mock
    private HomeContentValidator validator;

    @Mock
    private RuleContentFiller ruleContentFiller;

    @Mock
    private TitleEngine titleService;

    @Mock
    private SectionFunctionEngine functionEngine;

    @Mock
    private GeoService geoService;
    @Mock
    private TitleEngine titleEngine;
    @Mock
    private NoHistoryEngine noHistoryEngine;
    @Mock
    private SectionRuleEngine sectionEngine;
    @Mock
    private LastResortEngine lastResortEngine;
	@Mock
    private UserContext userContext;
	@Mock
	private UserRecord userRecord;


    @Before
    public void before() {

        when(this.geoService.normalizeIata(Matchers.anyString())).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                return (String) args[0];
            }
        });

        Mockito.when(this.userContext.getUserRecord()).thenReturn(this.userRecord);
        Mockito.when(this.userRecord.getLastIntenseSearch()).thenReturn(null);
        Mockito.doNothing().when(this.validator).//
            validate(Matchers.any(Product.class), //
                Matchers.any(HomeContent.class),//
                Matchers.any(ActionRecommendation.class));

        // rule content filler just returns the same RuleContent
        Mockito.when(
            this.ruleContentFiller.buildRuleContent(Matchers.any(SectionRuleContent.class),
                Matchers.any(ActionRecommendation.class))).thenAnswer(new Answer<RuleContentPriority>() {
            @Override
            public RuleContentPriority answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                SectionRuleContent rc = (SectionRuleContent) args[0];
                return RuleContentPriority.fromRuleContent(rc);
            }
        });

        Mockito
            .doNothing()
            .when(this.titleService)
            .updateTitle(Matchers.any(ActionRecommendation.class), Matchers.any(RuleSection.class),
                Matchers.any(Offer.class));
    }



    @Test
    public void testSearch() {

        Mockito.//
            when(this.functionEngine.getSectionFunction(Mockito.any(Function.class)))
            //
            .thenReturn(
                this.buildSectionFunction(SectionFunctionCode.HOT_RANKING_IP_COUNTRY, OFFER, HIGHTLIGHTED, CELL1, CELL2,
                    CELL3),
                this.buildSectionFunction(SectionFunctionCode.CARS_RANKING_COUNTRY, OFFER, HIGHTLIGHTED, CELL1, CELL2, CELL3));

        SectionRuleEngine sectionEngine = new SectionRuleEngine(this.noHistoryEngine, this.ruleContentFiller,
            this.functionEngine, this.titleEngine, this.validator, this.lastResortEngine);

        SectionRuleContent ruleFlightsContent = SectionRuleBuilder.create()//
            .withOffer(Product.FLIGHTS, Function.RANKING_ANY)//
            .withRow1(Product.FLIGHTS, Function.RANKING_ANY)//
            .withRow2(Product.CARS, Function.RANKING_ANY)//
            .withRow3(Product.HOTELS, Function.RANKING_ANY).build();

        RuleDefSection rta1 = RuleDefSection.builder() //
            .activity(ActivityType.NO_HISTORY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .home(HomeSupport.ANY)//
            .bought(BuyProductSupport.NONE)//
            .support(CountrySupport.SOME)//
            .build();


        SectionRuleContent ruleCarPriceMin = SectionRuleBuilder.create()//
            .withOffer(Product.FLIGHTS, Function.CAR_RANKING_COUNTRY_SEARCH)//
            .withRow1(Product.CARS, Function.CAR_RANKING_COUNTRY_SEARCH)//
            .withRow2(Product.FLIGHTS, Function.CAR_RANKING_COUNTRY_SEARCH)//
            .withRow3(Product.HOTELS, Function.CAR_RANKING_COUNTRY_SEARCH).build();

        RuleDefSection rta2 = RuleDefSection.builder() //
            .activity(ActivityType.SEARCH)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .home(HomeSupport.ANY)//
            .bought(BuyProductSupport.NONE)//
            .support(CountrySupport.SOME)//
            .build();

        this.add(rta1, ruleFlightsContent, sectionEngine);
        this.add(RuleDefSection.forAllCountries(rta1), ruleFlightsContent, sectionEngine);
        this.add(RuleDefSection.forLastResortAll(rta1), ruleFlightsContent, sectionEngine);
        this.add(RuleDefSection.forLastResortSome(rta1), ruleFlightsContent, sectionEngine);
        this.add(rta2, ruleCarPriceMin, sectionEngine);
        this.add(RuleDefSection.forAllCountries(rta2), ruleCarPriceMin, sectionEngine);

        EngineFacade engine = new EngineFacade(Lists.<RuleEngine> newArrayList(sectionEngine), this.lastResortEngine,
            this.noHistoryEngine, this.validator);

        engine.start();

        ActionRecommendation context = TestFuntions.buildActionRecommendation4Search(Product.FLIGHTS, "MIA");
        HomeContent homeContent = engine.buildHomeContent(context);

        this.assertHomeContent(homeContent, Product.HOTELS);

        HomeContent homeContent2 = engine.buildHomeContent(TestFuntions.buildActionRecommendation4Search(Product.HOTELS,
            "MIA"));

        this.assertHomeContent(homeContent2, Product.HOTELS);
    }

    @Test
    public void testBuy() {

        Mockito.//
            when(this.functionEngine.getSectionFunction(Mockito.eq(Function.RANKING_ANY)))//
            .thenReturn(
                this.buildSectionFunction(SectionFunctionCode.HOT_RANKING_IP_COUNTRY, OFFER, HIGHTLIGHTED, CELL1, CELL2,
                    CELL3));


        SectionRuleEngine sectionEngine = new SectionRuleEngine(this.noHistoryEngine, this.ruleContentFiller,
            this.functionEngine, this.titleEngine, this.validator, this.lastResortEngine);
        EngineFacade engine = new EngineFacade(Lists.<RuleEngine> newArrayList(sectionEngine), this.lastResortEngine,
            this.noHistoryEngine, this.validator);


        SectionRuleContent ruleFlightsContent = SectionRuleBuilder.create()//
            .withOffer(Product.FLIGHTS, Function.RANKING_ANY)//
            .withRow1(Product.CARS, Function.RANKING_ANY)//
            .withRow2(Product.FLIGHTS, Function.RANKING_ANY)//
            .withRow3(Product.HOTELS, Function.RANKING_ANY).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.BUY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .home(HomeSupport.ANY)//
            .bought(BuyProductSupport.HOTELS)//
            .support(CountrySupport.SOME)//
            .build();

        this.add(rta, ruleFlightsContent, sectionEngine);
        this.add(RuleDefSection.forAllCountries(rta), ruleFlightsContent, sectionEngine);
        this.add(RuleDefSection.forLastResortSome(rta), ruleFlightsContent, sectionEngine);
        this.add(RuleDefSection.forLastResortAll(rta), ruleFlightsContent, sectionEngine);


        engine.start();

        final ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.HOTELS, "MIA");
        action.setUserContext(this.userContext);
        HomeContent homeContent = engine.buildHomeContent(action);

        this.assertHomeContent(homeContent, Product.HOTELS);
    }

    @Test(expected = IllegalStateException.class)
    public void testEngineHasNotStarted() {

        Mockito.//
            when(this.functionEngine.getSectionFunction(Mockito.eq(Function.RANKING_ANY)))//
            .thenReturn(
                this.buildSectionFunction(SectionFunctionCode.HOT_RANKING_IP_COUNTRY, OFFER, HIGHTLIGHTED, CELL1, CELL2,
                    CELL3));


        SectionRuleEngine sectionEngine = new SectionRuleEngine(this.noHistoryEngine, this.ruleContentFiller,
            this.functionEngine, this.titleEngine, this.validator, this.lastResortEngine);
        EngineFacade engine = new EngineFacade(Lists.<RuleEngine> newArrayList(sectionEngine), this.lastResortEngine,
            this.noHistoryEngine, this.validator);


        engine.buildHomeContent(new ActionRecommendation("1", Product.CARS, CountryCode.AR, Language.ES, InetAddresses
            .forString("123.0.0.1"), RulesVersion.DYNAMIC_SERVICE, null, null));
    }

    @Test(expected = RuntimeException.class)
    public void testEngineHasStarted() {

        Mockito.//
            when(this.functionEngine.getSectionFunction(Mockito.eq(Function.RANKING_ANY)))//
            .thenReturn(
                this.buildSectionFunction(SectionFunctionCode.HOT_RANKING_IP_COUNTRY, OFFER, HIGHTLIGHTED, CELL1, CELL2,
                    CELL3));


        SectionRuleEngine sectionEngine = new SectionRuleEngine(this.noHistoryEngine, this.ruleContentFiller,
            this.functionEngine, this.titleEngine, this.validator, this.lastResortEngine);
        EngineFacade engine = new EngineFacade(Lists.<RuleEngine> newArrayList(sectionEngine), this.lastResortEngine,
            this.noHistoryEngine, this.validator);


        SectionRuleContent ruleFlightsContent = SectionRuleBuilder.create()//
            .withOffer(Product.FLIGHTS, Function.RANKING_ANY)//
            .withRow1(Product.FLIGHTS, Function.RANKING_ANY)//
            .withRow2(Product.FLIGHTS, Function.RANKING_ANY)//
            .withRow3(Product.HOTELS, Function.RANKING_ANY).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.BUY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .home(HomeSupport.ANY)//
            .bought(BuyProductSupport.HOTELS)//
            .support(CountrySupport.SOME)//
            .build();

        engine.start();


        this.add(rta, ruleFlightsContent, sectionEngine);
        this.add(RuleDefSection.forAllCountries(rta), ruleFlightsContent, sectionEngine);
        this.add(RuleDefSection.forLastResortAll(rta), ruleFlightsContent, sectionEngine);
        this.add(RuleDefSection.forLastResortSome(rta), ruleFlightsContent, sectionEngine);



        final ActionRecommendation action1 = TestFuntions.buildActionRecommendation4Buy(Product.HOTELS, "MIA");


        HomeContent homeContent = engine.buildHomeContent(action1);

        this.assertHomeContent(homeContent, Product.HOTELS);

    }


    /**
     * For a country that is not supporting Closed Packages we use the {@link CountrySupport}.ALL rule.
     */
    @Test
    public void testProductNotSupportedInCountry() {

        Mockito.//
            when(this.functionEngine.getSectionFunction(Mockito.eq(Function.RANKING_ANY)))//
            .thenReturn(
                this.buildSectionFunction(SectionFunctionCode.HOT_RANKING_IP_COUNTRY, OFFER, HIGHTLIGHTED, CELL1, CELL2,
                    CELL3));


        SectionRuleEngine sectionEngine = new SectionRuleEngine(this.noHistoryEngine, this.ruleContentFiller,
            this.functionEngine, this.titleEngine, this.validator, this.lastResortEngine);
        EngineFacade engine = new EngineFacade(Lists.<RuleEngine> newArrayList(sectionEngine), this.lastResortEngine,
            this.noHistoryEngine, this.validator);;

        SectionRuleContent ruleFlightsContent = SectionRuleBuilder.create()//
            .withOffer(Product.FLIGHTS, Function.RANKING_ANY)//
            .withRow1(Product.CLOSED_PACKAGES, Function.RANKING_ANY)//
            .withRow2(Product.FLIGHTS, Function.RANKING_ANY)//
            .withRow3(Product.HOTELS, Function.RANKING_ANY).build();

        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.NO_HISTORY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .home(HomeSupport.HOTELS)//
            .bought(BuyProductSupport.NONE)//
            .support(CountrySupport.SOME)//
            .build();


        SectionRuleContent rcDefault = SectionRuleBuilder.copy(ruleFlightsContent)//
            .overOffer(Product.HOTELS, Function.RANKING_ANY)//
            .overRow1(Product.HOTELS, Function.RANKING_ANY)//
            .overRow2(Product.HOTELS, Function.RANKING_ANY)//
            .build();

        this.add(rta, ruleFlightsContent, sectionEngine);
        this.add(RuleDefSection.forAllCountries(rta), rcDefault, sectionEngine);
        this.add(RuleDefSection.forLastResortSome(rta), ruleFlightsContent, sectionEngine);
        this.add(RuleDefSection.forLastResortAll(rta), rcDefault, sectionEngine);

        engine.start();

        final ActionRecommendation action1 = new ActionRecommendation("1", Product.HOTELS, CountryCode.BO, Language.ES,
            InetAddresses.forString("127.0.0.1"), RulesVersion.DYNAMIC_SERVICE, null, null);
        action1.setForceRulesversion(RulesVersion.DYNAMIC_SERVICE);
        List<RowHome> rowModules = new ArrayList<RowHome>();
        List<HotelDestinationItem> offers = new ArrayList<HotelDestinationItem>();
        offers.add(new HotelDestinationItem("BUE"));
        rowModules.add(new RowHome(new HotelItem("23456"), offers));
        List<Offer> specialOffersModule = new ArrayList<Offer>();
        specialOffersModule.add(new Offer(new HotelDestinationItem("s"), null));
        HomeProduct homeProduct = new HomeProduct(specialOffersModule, rowModules);
        Mockito.when(this.noHistoryEngine.buildHomeForProduct(action1, Product.HOTELS)).thenReturn(homeProduct);
        HomeContent homeContent = engine.buildHomeContent(action1);

        // should be overriden
        RowHome rowHome = homeContent.getProducts().get(Product.HOTELS).getRowModules().get(0);
        Assert.assertNotNull(rowHome);
        Assert.assertTrue(rowHome.getOffers().get(0) instanceof HotelDestinationItem);
    }



    /**
     * @param homeContent
     */
    private void assertHomeContent(HomeContent homeContent, Product pr) {
        Assert.assertTrue(homeContent.isSingleProduct());
        Assert.assertEquals(1, homeContent.getProducts().size());
        Assert.assertTrue(homeContent.getProducts().containsKey(pr));

        HomeProduct homeProduct = homeContent.getProducts().get(pr);

        Assert.assertEquals(OFFER, homeProduct.getSpecialOffersModule().get(0).getOffer());
        Assert.assertEquals(3, homeProduct.getRowModules().size());

        this.assetRowHome(homeProduct.getRowModules().get(0));
        this.assetRowHome(homeProduct.getRowModules().get(1));
        this.assetRowHome(homeProduct.getRowModules().get(2));
    }

    /**
     * @param row1
     */
    private void assetRowHome(final RowHome row1) {
        Assert.assertEquals(HIGHTLIGHTED, row1.getHighlighted());
        Assert.assertEquals(3, row1.getOffers().size());
        Assert.assertEquals(CELL1, row1.getOffers().get(0));
        Assert.assertEquals(CELL2, row1.getOffers().get(1));
        Assert.assertEquals(CELL3, row1.getOffers().get(2));
    }

    private SectionFunctionWrapper buildSectionFunction(final SectionFunctionCode code, final ItemHome offer,
        final ItemHome highlighted, final ItemHome... offers) {

        BaseSectionFunction function = new BaseSectionFunction() {

            @Override
            public SectionFunctionCode getFunctionCode() {
                return code;
            }

            @Override
            public String getDescription() {
                return "";
            }

            @SuppressWarnings("unused")
			public List<Offer> buildOffers(Product home, Product pr, ActionRecommendation action, Param param) {
                return Lists.newArrayList(new Offer(offer, null));
            }
            @SuppressWarnings("unused")
            public RowHome buildRow(Product home, Product pr, ActionRecommendation action, Param param) {
                return new RowHome(highlighted, Arrays.asList(offers));
            }

        };

        return new SectionFunctionWrapper(function);
    }

    private void add(RuleDefSection rule, SectionRuleContent rc, SectionRuleEngine engine) {

        for (RulesVersion version : RulesVersion.ENABLED) {
            rule = RuleDefSection.copy(rule);
            switch (version) {
            case DYNAMIC_SERVICE:
                engine.addForDynamicServ(rule, rc);
                break;
            case DYNAMIC_PRODUCT:
                engine.addForDynamicPr(rule, rc);
                break;
            default:
                break;
            }
        }
    }
}
