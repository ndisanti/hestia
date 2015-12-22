package ar.com.despegar.p13n.hestia;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Language;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.UserContext;
import com.despegar.p13n.euler.commons.client.model.UserLocation;
import com.despegar.p13n.hestia.api.data.model.HomeContent;
import com.despegar.p13n.hestia.api.data.model.HomeProduct;
import com.despegar.p13n.hestia.api.data.model.HotelDestinationItem;
import com.despegar.p13n.hestia.api.data.model.HotelItem;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.ItemType;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.api.data.model.RulesVersion;
import com.despegar.p13n.hestia.external.geo.GeoService;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendationBuilder;
import com.despegar.p13n.hestia.recommend.allinone.HomeContentRuleService;
import com.despegar.p13n.hestia.recommend.allinone.HomeParam;
import com.despegar.p13n.hestia.recommend.allinone.LastResortProfilService;
import com.despegar.p13n.hestia.recommend.allinone.SearchBoxRecommender;
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
import com.despegar.p13n.hestia.recommend.allinone.rules.section.BaseSectionFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Function;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionWrapper;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent.RuleDefSection;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent.SectionRuleContent;
import com.despegar.p13n.hestia.recommend.allinone.title.TitleEngine;
import com.google.common.collect.Lists;
import com.google.common.net.InetAddresses;

@RunWith(MockitoJUnitRunner.class)
public class HomeContentRuleServiceTest
    extends MockitoAnnotationBaseTest {

    @Mock
    private SearchBoxRecommender searchBoxRecommender;

    @InjectMocks
    private HomeContentRuleService ruleService = new HomeContentRuleService();

    @Mock
    private ActionRecommendationBuilder builder;

    @Mock
    private RuleContentFiller ruleContentFiller;

    @Mock
    private HomeContentValidator validator;

    @Mock
    private TitleEngine titleEngine;

    @Mock
    private SectionFunctionEngine functionEngine;

    @Mock
    private GeoService geoService;

    @Mock
    private NoHistoryEngine noHistoryEngine;
    @Mock
    private LastResortEngine lastResortEngine;

    @Mock
    private LastResortProfilService profileService;

    private ActionRecommendation action;

    @Mock
    private UserContext userContextAccesor;

    @Mock
    private UserLocation userLocation;


    @Before
    public void before() {

        this.ruleService.setHandleTimeOut(false);
        this.ruleService.setHomeAsyncExecutorService(Executors.newFixedThreadPool(1));
        this.ruleService.setMillisToTimeout(1);

        this.action = new ActionRecommendation("12345", Product.HOTELS, CountryCode.AR, Language.ES,
            InetAddresses.forString("123.123.123.123"), RulesVersion.DYNAMIC_SERVICE, null, null);

        this.action.setUserContext(this.userContextAccesor);

        Mockito.when(this.userContextAccesor.getUserLocation()).thenReturn(this.userLocation);
        Mockito.when(this.userLocation.getCity()).thenReturn("BUE");

        Mockito.when(this.builder.buildActionRecommendation(Mockito.any(HomeParam.class))).thenReturn(this.action);

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


        this.ruleService.setActionBuilder(this.builder);

        Mockito.when(this.functionEngine.getSectionFunction(Matchers.any(Function.class))).thenAnswer(
            new Answer<SectionFunction>() {
                @Override
                public SectionFunction answer(InvocationOnMock invocation) throws Throwable {
                    return new SectionFunctionWrapper(new SampleFunction());
                }
            });


        Mockito.doNothing().when(this.validator).//
            validate(Matchers.any(Product.class), //
                Matchers.any(HomeContent.class), //
                Matchers.any(ActionRecommendation.class));


        Mockito.when(this.validator.isLastResortEnabled()).thenReturn(true);

        Mockito
            .doNothing()
            .when(this.titleEngine)
            .updateTitle(Matchers.any(ActionRecommendation.class), Matchers.any(RuleSection.class),
                Matchers.any(Offer.class));

        Mockito.when(this.geoService.normalizeIata(Matchers.anyString())).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                String iata = (String) args[0];
                return iata;
            }
        });



        SectionRuleEngine sectionRuleEngine = new SectionRuleEngine(this.noHistoryEngine, this.ruleContentFiller,
            this.functionEngine, this.titleEngine, this.validator, this.lastResortEngine);

        EngineFacade engine = new EngineFacade(Lists.<RuleEngine> newArrayList(sectionRuleEngine), this.lastResortEngine,
            this.noHistoryEngine, this.validator);

        SectionRuleContent ruleContent = SectionRuleBuilder.create()//
            .withOffer(Product.HOTELS, Function.BUY_DESTINATION)//
            .withRow1(Product.HOTELS, Function.BUY_DESTINATION)//
            .withRow2(Product.HOTELS, Function.BUY_DESTINATION)//
            .withRow3(Product.HOTELS, Function.BUY_DESTINATION).build();


        RuleDefSection rta = RuleDefSection.builder() //
            .activity(ActivityType.NO_HISTORY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .home(HomeSupport.HOTELS)//
            .support(CountrySupport.SOME)//
            .bought(BuyProductSupport.NONE)//
            .build();

        rta = RuleDefSection.copy(rta);
        for (RulesVersion version : RulesVersion.ENABLED) {

            switch (version) {
            case DYNAMIC_PRODUCT:
                sectionRuleEngine.addForDynamicPr(rta, ruleContent);
                sectionRuleEngine.addForDynamicPr(RuleDefSection.forAllCountries(rta), ruleContent);
                break;

            case DYNAMIC_SERVICE:
                sectionRuleEngine.addForDynamicServ(rta, ruleContent);
                sectionRuleEngine.addForDynamicServ(RuleDefSection.forAllCountries(rta), ruleContent);
                break;

            default:
                break;
            }
        }

        engine.start();
        this.ruleService.setEngineFacade(engine);
    }

    @Test
    public void test() throws Exception {
        List<RowHome> rowModules = new ArrayList<RowHome>();
        rowModules.add(new RowHome(new HotelItem("123456"), null));
        List<Offer> specialOffersModule = new ArrayList<Offer>();
        specialOffersModule.add(new Offer(new HotelDestinationItem("BUE"), null));
        HomeProduct homeProduct = new HomeProduct(specialOffersModule, rowModules);
        HomeParam homeParam = new HomeParam(CountryCode.AR, InetAddresses.forString("123.123.123.123"),
            EnumSet.of(Product.HOTELS), "12345", Language.ES, false, false, null, null, null);
        Mockito.when(this.lastResortEngine.getLastResortDefault(Product.HOTELS, CountryCode.AR, Language.ES)).thenReturn(
            homeProduct);
        HomeContent homeContent = new HomeContent();
        Map<Product, HomeProduct> products = new HashMap<Product, HomeProduct>();
        products.put(Product.HOTELS, homeProduct);
        homeContent.setProducts(products);
        HomeContent content = this.ruleService.getContent(homeParam);
        Assert.assertNotNull(content);
        Assert.assertTrue(content.getProducts().containsKey(Product.HOTELS));
        Assert.assertEquals(ItemType.HOTEL_DESTINATION, content.getProducts().get(Product.HOTELS).getSpecialOffersModule()
            .get(0).getOffer().getOfferType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProductNoSupported() throws Exception {

        this.ruleService.getContent(new HomeParam(CountryCode.AR, InetAddresses.forString("123.123.123.123"), EnumSet
            .of(Product.SKI_AS_PRODUCT), "12345", Language.ES, false, false, null, null, null));

    }

    private class SampleFunction
        extends BaseSectionFunction {

        @Override
        public List<Offer> buildOffers(Product home, Product pr, ActionRecommendation actionRecommendation, Param param) {
            Offer offer = new Offer(this.buildDestination(pr, param.getRankingType(), "MIA", null, actionRecommendation),
                null);
            return Lists.newArrayList(offer);
        }

        @Override
        public RowHome buildRow(Product home, Product pr, ActionRecommendation actionRecommendation, Param param) {

            List<ItemHome> items = Lists.newArrayList(
                this.buildDestination(pr, param.getRankingType(), "BUE", null, actionRecommendation),
                this.buildDestination(pr, param.getRankingType(), "NYC", null, actionRecommendation));
            return new RowHome(null, items);
        }

        @Override
        public SectionFunctionCode getFunctionCode() {
            return SectionFunctionCode.BUY_DESTINATION;
        }

        @Override
        public GeoService getGeoService() {
            return HomeContentRuleServiceTest.this.geoService;
        }


        @Override
        public String getDescription() {
            return "Sample";
        }

    }


    @Test
    public void testTimeout() throws Exception {
        this.ruleService.setHandleTimeOut(true);
        this.ruleService.setMillisToTimeout(0);
        this.ruleService.setHomeAsyncExecutorService(Executors.newFixedThreadPool(1));
        List<RowHome> rowModules = new ArrayList<RowHome>();
        rowModules.add(new RowHome(new HotelItem("123456"), null));
        List<Offer> specialOffersModule = new ArrayList<Offer>();
        specialOffersModule.add(new Offer(new HotelDestinationItem("BUE"), null));
        HomeProduct homeProduct = new HomeProduct(specialOffersModule, rowModules);
        Mockito.when(this.lastResortEngine.getLastResortDefault(Product.CARS, CountryCode.AR, Language.ES)).thenReturn(
            homeProduct);
        HomeContent homeContent = new HomeContent();
        Map<Product, HomeProduct> products = new HashMap<Product, HomeProduct>();
        products.put(Product.CARS, homeProduct);
        homeContent.setProducts(products);
        HomeContent content = this.ruleService.getContent(new HomeParam(CountryCode.AR, InetAddresses
            .forString("123.123.123.123"), EnumSet.of(Product.CARS), "12345", Language.ES, false, false, null, null, null));

        Assert.assertTrue(content.getProducts().containsKey(Product.CARS));
        Assert.assertEquals(ItemType.HOTEL_DESTINATION, content.getProducts().get(Product.CARS).getSpecialOffersModule()
            .get(0).getOffer().getOfferType());

    }
}
