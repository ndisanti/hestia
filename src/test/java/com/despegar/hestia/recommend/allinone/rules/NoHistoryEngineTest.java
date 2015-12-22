package com.despegar.hestia.recommend.allinone.rules;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ar.com.despegar.p13n.hestia.TestFuntions;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.ClosedPackagesDestinationItem;
import com.despegar.p13n.hestia.api.data.model.ClosedPackagesItem;
import com.despegar.p13n.hestia.api.data.model.FlightDestinationItem;
import com.despegar.p13n.hestia.api.data.model.HomeProduct;
import com.despegar.p13n.hestia.api.data.model.ItemType;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.api.data.model.Title;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.City;
import com.despegar.p13n.hestia.recommend.allinone.rules.NoHistoryEngine;
import com.despegar.p13n.hestia.recommend.allinone.rules.SectionFunctionEngine;
import com.despegar.p13n.hestia.recommend.allinone.rules.SectionsEnum;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Function;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Function.CarRankingType;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Function.CountryType;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.functions.HotRankingIpCountryFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent.QueryRuleSection;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent.SectionRuleContent;
import com.despegar.p13n.hestia.recommend.allinone.rules.version.NoHistoryRules;
import com.despegar.p13n.hestia.recommend.allinone.title.TitleEngine;

@RunWith(MockitoJUnitRunner.class)
public class NoHistoryEngineTest {

    private NoHistoryEngine engine;
    @Mock
    private TitleEngine titleEngine;
    @Mock
    private SectionFunctionEngine functionEngine;

    @Before
    public void setUp() {
        this.engine = new NoHistoryEngine(this.titleEngine, this.functionEngine);
        new NoHistoryRules(this.engine);
    }

    @Test
    public void testGetNoHistoryPriorityForCars() {

        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.CARS, "MIA");
        SectionRuleContent originalRuleContent = this.engine.getNoHistoryPriority(action, Product.CARS);
        Assert.assertNotNull(originalRuleContent);
        Assert.assertTrue(originalRuleContent.getOffer().getFunction().equals(Function.CAR_RANKING_COUNTRY_SEARCH));
        Assert.assertTrue(originalRuleContent.getOffer().getProduct().equals(Product.CARS));
        Assert.assertTrue(originalRuleContent
            .getRow1()
            .getFunction()
            .equals(
                Function.CAR_RANKING_COUNTRY_SEARCH.carRankingType(CarRankingType.SEARCH).countryType(
                    CountryType.INTERNATIONAL)));
        Assert.assertTrue(originalRuleContent.getRow1().getProduct().equals(Product.CARS));
        Assert
            .assertTrue(originalRuleContent
                .getRow2()
                .getFunction()
                .equals(
                    Function.CAR_RANKING_COUNTRY_SEARCH.carRankingType(CarRankingType.SEARCH).countryType(
                        CountryType.DOMESTIC)));
        Assert.assertTrue(originalRuleContent.getRow2().getProduct().equals(Product.CARS));
        Assert.assertTrue(originalRuleContent.getRow3().getFunction().equals(Function.RANKING_ANY));
        Assert.assertTrue(originalRuleContent.getRow3().getProduct().equals(Product.HOTELS));
    }

    @Test
    public void testGetNoHistoryPriorityForActivities() {

        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.ACTIVITIES, "MIA");
        action.setCountryCode(CountryCode.BR);
        SectionRuleContent originalRuleContent = this.engine.getNoHistoryPriority(action, Product.ACTIVITIES);
        Assert.assertNotNull(originalRuleContent);
        Assert.assertEquals(Function.ACTIVITIES_DISNEY, originalRuleContent.getOffer().getFunction());
        Assert.assertEquals(Product.ACTIVITIES, originalRuleContent.getOffer().getProduct());
        Assert.assertEquals(Function.ACTIVITIES_UNIVERSAL.city(City.ORLANDO), originalRuleContent.getRow1().getFunction());
        Assert.assertEquals(Product.ACTIVITIES, originalRuleContent.getRow1().getProduct());
        Assert.assertEquals(Function.RANKING_INTERNATIONAL, originalRuleContent.getRow2().getFunction());
        Assert.assertEquals(Product.ACTIVITIES, originalRuleContent.getRow2().getProduct());
        Assert.assertEquals(Function.RANKING_DOMESTIC, originalRuleContent.getRow3().getFunction());
        Assert.assertEquals(Product.ACTIVITIES, originalRuleContent.getRow3().getProduct());
    }

    @Test
    public void testGetNoHistoryPriorityForActivitiesInArgentina() {

        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.ACTIVITIES, "MIA");
        SectionRuleContent originalRuleContent = this.engine.getNoHistoryPriority(action, Product.ACTIVITIES);
        Assert.assertNotNull(originalRuleContent);
        Assert.assertEquals(Function.ACTIVITIES_DISNEY, originalRuleContent.getOffer().getFunction());
        Assert.assertEquals(Product.ACTIVITIES, originalRuleContent.getOffer().getProduct());
        Assert.assertEquals(Function.ACTIVITIES_UNIVERSAL.city(City.ORLANDO), originalRuleContent.getRow1().getFunction());
        Assert.assertEquals(Product.ACTIVITIES, originalRuleContent.getRow1().getProduct());
        Assert.assertEquals(Function.RANKING_INTERNATIONAL, originalRuleContent.getRow2().getFunction());
        Assert.assertEquals(Product.ACTIVITIES, originalRuleContent.getRow2().getProduct());
        Assert.assertEquals(Function.RANKING_DOMESTIC, originalRuleContent.getRow3().getFunction());
        Assert.assertEquals(Product.ACTIVITIES, originalRuleContent.getRow3().getProduct());
    }

    @Test
    public void testGetNoHistoryPriorityForFlights() {

        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.FLIGHTS, "MIA");
        SectionRuleContent originalRuleContent = this.engine.getNoHistoryPriority(action, Product.FLIGHTS);
        Assert.assertNotNull(originalRuleContent);
        Assert.assertTrue(originalRuleContent.getOffer().getFunction().equals(Function.RANKING_INTERNATIONAL));
        Assert.assertTrue(originalRuleContent.getOffer().getProduct().equals(Product.FLIGHTS));
        Assert.assertTrue(originalRuleContent.getRow1().getFunction().equals(Function.RANKING_INTERNATIONAL));
        Assert.assertTrue(originalRuleContent.getRow1().getProduct().equals(Product.FLIGHTS));
        Assert.assertTrue(originalRuleContent.getRow2().getFunction().equals(Function.RANKING_DOMESTIC));
        Assert.assertTrue(originalRuleContent.getRow2().getProduct().equals(Product.FLIGHTS));
        Assert.assertTrue(originalRuleContent.getRow3().getFunction().equals(Function.RANKING_ANY));
        Assert.assertTrue(originalRuleContent.getRow3().getProduct().equals(Product.HOTELS));
        action.setCountryCode(CountryCode.PR);
        originalRuleContent = this.engine.getNoHistoryPriority(action, Product.FLIGHTS);
        Assert.assertNotNull(originalRuleContent);
        Assert.assertTrue(originalRuleContent.getOffer().getFunction().equals(Function.RANKING_INTERNATIONAL));
        Assert.assertTrue(originalRuleContent.getOffer().getProduct().equals(Product.FLIGHTS));
        Assert.assertTrue(originalRuleContent.getRow1().getFunction().equals(Function.RANKING_INTERNATIONAL));
        Assert.assertTrue(originalRuleContent.getRow1().getProduct().equals(Product.FLIGHTS));
        Assert.assertTrue(originalRuleContent.getRow2().getFunction().equals(Function.RANKING_INTERNATIONAL));
        Assert.assertTrue(originalRuleContent.getRow2().getProduct().equals(Product.FLIGHTS));
        Assert.assertTrue(originalRuleContent.getRow3().getFunction().equals(Function.RANKING_ANY));
        Assert.assertTrue(originalRuleContent.getRow3().getProduct().equals(Product.HOTELS));


    }

    @Test
    public void testGetNoHistoryPriorityForHotels() {

        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.HOTELS, "MIA");
        SectionRuleContent originalRuleContent = this.engine.getNoHistoryPriority(action, Product.HOTELS);
        Assert.assertNotNull(originalRuleContent);
        Assert.assertTrue(originalRuleContent.getOffer().getFunction().equals(Function.RANKING_INTERNATIONAL));
        Assert.assertTrue(originalRuleContent.getOffer().getProduct().equals(Product.HOTELS));
        Assert.assertTrue(originalRuleContent.getRow1().getFunction().equals(Function.RANKING_INTERNATIONAL));
        Assert.assertTrue(originalRuleContent.getRow1().getProduct().equals(Product.HOTELS));
        Assert.assertTrue(originalRuleContent.getRow2().getFunction().equals(Function.RANKING_DOMESTIC));
        Assert.assertTrue(originalRuleContent.getRow2().getProduct().equals(Product.HOTELS));
        Assert.assertTrue(originalRuleContent.getRow3().getFunction().equals(Function.RANKING_ANY));
        Assert.assertTrue(originalRuleContent.getRow3().getProduct().equals(Product.CLOSED_PACKAGES));
    }


    @Test
    public void testGetNoHistoryPriorityForCruises() {

        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.CRUISES, "MIA");
        SectionRuleContent originalRuleContent = this.engine.getNoHistoryPriority(action, Product.CRUISES);
        Assert.assertNotNull(originalRuleContent);
        Assert.assertTrue(originalRuleContent.getOffer().getFunction().equals(Function.RANKING_REGION));
        Assert.assertTrue(originalRuleContent.getOffer().getProduct().equals(Product.CRUISES));
        Assert.assertTrue(originalRuleContent.getRow1().getFunction().equals(Function.RANKING_REGION));
        Assert.assertTrue(originalRuleContent.getRow1().getProduct().equals(Product.CRUISES));
        Assert.assertTrue(originalRuleContent.getRow2().getFunction().equals(Function.RANKING_REGION));
        Assert.assertTrue(originalRuleContent.getRow2().getProduct().equals(Product.CRUISES));
        Assert.assertTrue(originalRuleContent.getRow3().getFunction().equals(Function.RANKING_ANY));
        Assert.assertTrue(originalRuleContent.getRow3().getProduct().equals(Product.FLIGHTS));
    }

    @Test
    public void testGetNoHistoryPriorityForClosedPackages() {

        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.CLOSED_PACKAGES, "MIA");
        SectionRuleContent originalRuleContent = this.engine.getNoHistoryPriority(action, Product.CLOSED_PACKAGES);
        Assert.assertNotNull(originalRuleContent);
        Assert.assertTrue(originalRuleContent.getOffer().getFunction().equals(Function.RANKING_INTERNATIONAL));
        Assert.assertTrue(originalRuleContent.getOffer().getProduct().equals(Product.CLOSED_PACKAGES));
        Assert.assertTrue(originalRuleContent.getRow1().getFunction().equals(Function.RANKING_INTERNATIONAL));
        Assert.assertTrue(originalRuleContent.getRow1().getProduct().equals(Product.CLOSED_PACKAGES));
        Assert.assertTrue(originalRuleContent.getRow2().getFunction().equals(Function.RANKING_DOMESTIC));
        Assert.assertTrue(originalRuleContent.getRow2().getProduct().equals(Product.CLOSED_PACKAGES));
        Assert.assertTrue(originalRuleContent.getRow3().getFunction().equals(Function.RANKING_ANY));
        Assert.assertTrue(originalRuleContent.getRow3().getProduct().equals(Product.FLIGHTS));
    }

    @Test
    public void testBuildHomeForProduct() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.CLOSED_PACKAGES, "MIA");
        HotRankingIpCountryFunction function = Mockito.mock(HotRankingIpCountryFunction.class);
        Mockito.when(this.functionEngine.getSectionFunction(Function.RANKING_INTERNATIONAL)).thenReturn(function);
        Mockito.when(this.functionEngine.getSectionFunction(Function.RANKING_DOMESTIC)).thenReturn(function);
        Mockito.when(this.functionEngine.getSectionFunction(Function.RANKING_ANY)).thenReturn(function);
        List<ClosedPackagesItem> offers2 = new ArrayList<ClosedPackagesItem>();
        offers2.add(new ClosedPackagesItem("CLUID"));
        RowHome row = new RowHome(null, offers2);
        Mockito.when(
            function.buildRow(Product.CLOSED_PACKAGES, Product.CLOSED_PACKAGES, action,
                Function.RANKING_INTERNATIONAL.getParam())).thenReturn(row);
        Mockito
            .when(
                function.buildRow(Product.CLOSED_PACKAGES, Product.CLOSED_PACKAGES, action,
                    Function.RANKING_DOMESTIC.getParam())).thenReturn(row);
        Mockito.when(function.buildRow(Product.CLOSED_PACKAGES, Product.FLIGHTS, action, Function.RANKING_ANY.getParam()))
            .thenReturn(row);
        List<Offer> offers = new ArrayList<Offer>();
        offers.add(new Offer(new FlightDestinationItem("MIA", "BUE", "sss"), null));
        Mockito.when(
            function.buildOffers(Product.CLOSED_PACKAGES, Product.CLOSED_PACKAGES, action,
                Function.RANKING_INTERNATIONAL.getParam())).thenReturn(offers);
        HomeProduct homeProduct = this.engine.buildHomeForProduct(action, Product.CLOSED_PACKAGES);
        Assert.assertNotNull(homeProduct);
    }

	@Test
    public void testBuildOffers() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.CLOSED_PACKAGES, "MIA");
        HotRankingIpCountryFunction function = Mockito.mock(HotRankingIpCountryFunction.class);
        Mockito.when(this.functionEngine.getSectionFunction(Function.RANKING_INTERNATIONAL)).thenReturn(function);
        QueryRuleSection query = this.engine.buildQueryForNoHistory(Product.CLOSED_PACKAGES);
        SectionRuleContent sectionRuleContent = this.engine.getSectionRuleContent(action, query);
        List<Offer> offers = new ArrayList<Offer>();
        offers.add(new Offer(new ClosedPackagesDestinationItem("CUN", "BUE"), new Title("T18")));
        Mockito.when(
            function.buildOffers(Product.CLOSED_PACKAGES, Product.CLOSED_PACKAGES, action,
                Function.RANKING_INTERNATIONAL.getParam())).thenReturn(offers, null);
        List<Offer> obtainedOffers = this.engine.buildNoHistoryOffers(action, Product.CLOSED_PACKAGES, sectionRuleContent);
        Assert.assertNotNull(obtainedOffers);
        List<Offer> obtainedEmptyOffers = this.engine.buildNoHistoryOffers(action, Product.CLOSED_PACKAGES,
            sectionRuleContent);
        Assert.assertNull(obtainedEmptyOffers);
        Assert.assertTrue(obtainedOffers.get(0).getOffer().getOfferType().equals(ItemType.CLOSED_PACKAGES_DESTINATION));
        Assert.assertTrue(obtainedOffers.get(0).getOffer().getId().equals("CUN"));
    }

    @Test
    public void testBuildRow() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.CLOSED_PACKAGES, "MIA");
        HotRankingIpCountryFunction function = Mockito.mock(HotRankingIpCountryFunction.class);
        Mockito.when(this.functionEngine.getSectionFunction(Function.RANKING_INTERNATIONAL)).thenReturn(function);
        List<ClosedPackagesDestinationItem> offers = new ArrayList<ClosedPackagesDestinationItem>();
        offers.add(new ClosedPackagesDestinationItem("PUJ", "BUE"));
        offers.add(new ClosedPackagesDestinationItem("CFB", "BUE"));
        offers.add(new ClosedPackagesDestinationItem("ADZ", "BUE"));
        offers.add(new ClosedPackagesDestinationItem("FLN", "BUE"));
        offers.add(new ClosedPackagesDestinationItem("RIO", "BUE"));
        offers.add(new ClosedPackagesDestinationItem("NAT", "BUE"));
        offers.add(new ClosedPackagesDestinationItem("CZM", "BUE"));
        RowHome rowHome = new RowHome(null, offers);
        Mockito.when(
            function.buildRow(Product.CLOSED_PACKAGES, Product.CLOSED_PACKAGES, action,
                Function.RANKING_INTERNATIONAL.getParam())).thenReturn(rowHome);
        QueryRuleSection query = this.engine.buildQueryForNoHistory(Product.CLOSED_PACKAGES);
        SectionRuleContent sectionRuleContent = this.engine.getSectionRuleContent(action, query);
        RowHome row = this.engine.buildNoHistoryRow(action, sectionRuleContent, Product.CLOSED_PACKAGES, SectionsEnum.ROW1);
        Assert.assertNotNull(row);
        Assert.assertTrue(row.getOffers().size() == 7);
        Assert.assertTrue(row.getOffers().get(0).getOfferType().equals(ItemType.CLOSED_PACKAGES_DESTINATION));
        Assert.assertTrue(((ClosedPackagesDestinationItem) row.getOffers().get(0)).getDestination().equals("PUJ"));
        Assert.assertTrue(((ClosedPackagesDestinationItem) row.getOffers().get(1)).getDestination().equals("CFB"));
        Assert.assertTrue(((ClosedPackagesDestinationItem) row.getOffers().get(2)).getDestination().equals("ADZ"));
        Assert.assertTrue(((ClosedPackagesDestinationItem) row.getOffers().get(3)).getDestination().equals("FLN"));
        Assert.assertTrue(((ClosedPackagesDestinationItem) row.getOffers().get(4)).getDestination().equals("RIO"));
        Assert.assertTrue(((ClosedPackagesDestinationItem) row.getOffers().get(5)).getDestination().equals("NAT"));
        Assert.assertTrue(((ClosedPackagesDestinationItem) row.getOffers().get(6)).getDestination().equals("CZM"));
    }
}
