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
import com.despegar.p13n.euler.commons.client.model.Language;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.ClosedPackagesItem;
import com.despegar.p13n.hestia.api.data.model.FlightDestinationItem;
import com.despegar.p13n.hestia.api.data.model.HomeProduct;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.rules.HomeContentValidator;
import com.despegar.p13n.hestia.recommend.allinone.rules.LastResortEngine;
import com.despegar.p13n.hestia.recommend.allinone.rules.SectionFunctionEngine;
import com.despegar.p13n.hestia.recommend.allinone.rules.SectionsEnum;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Function;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.functions.LastResortFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent.QueryRuleSection;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent.SectionRuleContent;
import com.despegar.p13n.hestia.recommend.allinone.rules.version.LastResortRules;
import com.despegar.p13n.hestia.recommend.allinone.title.TitleEngine;
import com.despegar.p13n.hestia.service.MultiObjectVersionService;

@RunWith(MockitoJUnitRunner.class)
public class LastResortEngineTest {

    private LastResortEngine engine;
    @Mock
    private HomeContentValidator validator;
    @Mock
    private LastResortFunction lastResortFunction;
    @Mock
    private TitleEngine titleEngine;
    @Mock
    private MultiObjectVersionService moService;
    @Mock
    private SectionFunctionEngine functionEngine;


    @Before
    public void setUp() {
        this.engine = new LastResortEngine(this.validator, this.titleEngine, this.moService, this.functionEngine);
        new LastResortRules(this.engine);
    }

    @Test
    public void testBuildOffers() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.CLOSED_PACKAGES, "MIA");
        QueryRuleSection lastResortQuery = this.engine.getLastResortQuery(Product.CLOSED_PACKAGES);
        SectionRuleContent sectionRuleContent = this.engine.getSectionRuleContent(action, lastResortQuery);
        Mockito.when(this.functionEngine.getSectionFunction(Function.LAST_RESORT)).thenReturn(this.lastResortFunction);
        List<Offer> offers = new ArrayList<Offer>();
        offers.add(new Offer(new FlightDestinationItem("MIA", "BUE", "sss"), null));
        Mockito.when(
            this.lastResortFunction.buildOffers(Product.CLOSED_PACKAGES, Product.CLOSED_PACKAGES, action,
                Function.LAST_RESORT.getParam())).thenReturn(offers);
        List<Offer> obtainedOffers = this.engine.buildOffers(action, Product.CLOSED_PACKAGES, sectionRuleContent);
        Assert.assertNotNull(obtainedOffers);
    }

    @Test
    public void testBuildRow() {
        ActionRecommendation action = TestFuntions.buildActionRecommendation4Buy(Product.CLOSED_PACKAGES, "MIA");
        QueryRuleSection lastResortQuery = this.engine.getLastResortQuery(Product.CLOSED_PACKAGES);
        SectionRuleContent sectionRuleContent = this.engine.getSectionRuleContent(action, lastResortQuery);
        List<ClosedPackagesItem> offers = new ArrayList<ClosedPackagesItem>();
        offers.add(new ClosedPackagesItem("cluid11"));
        RowHome rowHome = new RowHome(null, offers);
        Mockito.when(this.functionEngine.getSectionFunction(Function.LAST_RESORT)).thenReturn(this.lastResortFunction);
        Mockito.when(
            this.lastResortFunction.buildRow(Product.CLOSED_PACKAGES, Product.HOTELS, action,
                Function.LAST_RESORT.getParam())).thenReturn(rowHome);
        RowHome row = this.engine.buildRow(action, sectionRuleContent, Product.CLOSED_PACKAGES, SectionsEnum.ROW1);
        Assert.assertNotNull(row);
    }

    @Test
    public void testBuildHome() {
        List<Offer> offers = new ArrayList<Offer>();
        offers.add(new Offer(new FlightDestinationItem("MIA", "BUE", "sss"), null));
        Mockito.when(
            this.lastResortFunction.buildOffers(Mockito.eq(Product.CLOSED_PACKAGES), Mockito.eq(Product.HOTELS),
                Mockito.any(ActionRecommendation.class), Mockito.eq(Function.LAST_RESORT.getParam()))).thenReturn(offers);
        List<ClosedPackagesItem> offers2 = new ArrayList<ClosedPackagesItem>();
        offers2.add(new ClosedPackagesItem("cluid11"));
        RowHome rowHome = new RowHome(null, offers2);
        Mockito.when(this.functionEngine.getSectionFunction(Function.LAST_RESORT)).thenReturn(this.lastResortFunction);
        Mockito.when(
            this.lastResortFunction.buildRow(Mockito.eq(Product.CLOSED_PACKAGES), Mockito.eq(Product.HOTELS),
                Mockito.any(ActionRecommendation.class), Mockito.eq(Function.LAST_RESORT.getParam()))).thenReturn(rowHome);
        HomeProduct result = this.engine.buildLastResoltHome(Product.CLOSED_PACKAGES, CountryCode.AR, Language.ES);
        Assert.assertNotNull(result);
    }


}
