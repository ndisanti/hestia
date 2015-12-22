package ar.com.despegar.p13n.hestia;

import java.util.EnumSet;

import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Language;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.HomeContent;
import com.despegar.p13n.hestia.api.data.model.RulesVersion;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.HomeParam;
import com.despegar.p13n.hestia.recommend.allinone.MabHomeDataService;
import com.despegar.p13n.hestia.recommend.allinone.RulesVersionService;
import com.despegar.p13n.hestia.recommend.allinone.rules.EngineFacade;
import com.despegar.p13n.hestia.service.MultiObjectVersionService;
import com.despegar.p13n.mab.MabService;

@RunWith(MockitoJUnitRunner.class)
public class MabHomeDataServiceTest {

    private MabHomeDataService mabHomeDataService;
    @Mock
    private EngineFacade engineFacade;
    private MabService mabService;
    @Mock
    private RulesVersionService rulesVersionService;
    @Mock
    private MultiObjectVersionService multiObjectService;



    @Before
    public void setUp() {
        this.mabService = new MabService();
        this.mabHomeDataService = new MabHomeDataService(this.mabService, this.engineFacade, this.rulesVersionService,
            this.multiObjectService);
    }

    @Test
    public void testCallEngineCountryCodeAR() {

        HomeParam homeParam = new HomeParam(CountryCode.AR, null, Product.HOTELS, "userID", Language.ES);
        ActionRecommendation action = new ActionRecommendation(homeParam);
        RulesVersion ruleVersion = RulesVersion.DYNAMIC_SERVICE;
        Mockito.when(this.rulesVersionService.getRulesVersion(CountryCode.AR)).thenReturn(ruleVersion);
        HomeContent homeContent = new HomeContent();
        Mockito.when(this.engineFacade.buildHomeContent(action)).thenReturn(homeContent);
        HomeContent obtainedResult = this.mabHomeDataService.callEngine(action);
        Assert.assertNotNull(obtainedResult);
        Mockito.verify(this.engineFacade).buildHomeContent(action);
        Mockito.verify(this.rulesVersionService).getRulesVersion(CountryCode.AR);
    }

    @Test
    public void testCallEngineCountryCodeInternational() {

        HomeParam homeParam = new HomeParam(CountryCode.GB, null, Product.HOTELS, "userID", Language.ES);
        ActionRecommendation action = new ActionRecommendation(homeParam);
        RulesVersion ruleVersion = RulesVersion.DYNAMIC_SERVICE;
        Mockito.when(this.rulesVersionService.getRulesVersion(CountryCode.GB)).thenReturn(ruleVersion);
        HomeContent homeContent = new HomeContent();
        Mockito.when(this.engineFacade.buildHomeContent(action)).thenReturn(homeContent);
        HomeContent obtainedResult = this.mabHomeDataService.callEngine(action);
        Assert.assertNotNull(obtainedResult);
        Mockito.verify(this.engineFacade).buildHomeContent(action);
        Mockito.verify(this.rulesVersionService).getRulesVersion(CountryCode.GB);
    }

    @Test
    public void testCallEngineCountryCodeCO() {
        HomeParam homeParam = new HomeParam(CountryCode.CO, null, Product.HOTELS, "userID", Language.ES);
        ActionRecommendation action = new ActionRecommendation(homeParam);
        HomeContent homeContent = new HomeContent();
        Mockito.when(this.engineFacade.buildHomeContent(action)).thenReturn(homeContent);
        HomeContent obtainedResult = this.mabHomeDataService.callEngine(action);
        Assert.assertNotNull(obtainedResult);
        Mockito.verify(this.engineFacade).buildHomeContent(action);
    }

    @Test
    public void testCallEngineCountryCodeEC() {
        HomeParam homeParam = new HomeParam(CountryCode.EC, null, Product.HOTELS, "userID", Language.ES);
        ActionRecommendation action = new ActionRecommendation(homeParam);
        HomeContent homeContent = new HomeContent();
        Mockito.when(this.engineFacade.buildHomeContent(action)).thenReturn(homeContent);
        HomeContent obtainedResult = this.mabHomeDataService.callEngine(action);
        Assert.assertNotNull(obtainedResult);
        Mockito.verify(this.engineFacade).buildHomeContent(action);
    }

    @Test
    public void testCallEnginForceRulesversion() {
        HomeParam homeParam = new HomeParam(CountryCode.AR, null, EnumSet.of(Product.HOTELS), "userId", Language.ES, false,
            false, null, RulesVersion.DYNAMIC_SERVICE, null);
        ActionRecommendation action = new ActionRecommendation(homeParam);
        HomeContent homeContent = new HomeContent();
        Mockito.when(this.engineFacade.buildHomeContent(action)).thenReturn(homeContent);
        HomeContent obtainedResult = this.mabHomeDataService.callEngine(action);
        Assert.assertNotNull(obtainedResult);
        Mockito.verify(this.engineFacade).buildHomeContent(action);
        Assert.assertTrue(action.getVersion().equals(RulesVersion.DYNAMIC_SERVICE));
        Assert.assertSame(action.getForceRulesversion(), action.getVersion());
        HomeParam homeParam2 = new HomeParam(CountryCode.AR, null, EnumSet.of(Product.HOTELS), "userId", Language.ES, false,
            false, null, null, null);
        ActionRecommendation action2 = new ActionRecommendation(homeParam2);
        Mockito.when(this.engineFacade.buildHomeContent(action2)).thenReturn(homeContent);
        RulesVersion ruleVersion = RulesVersion.DYNAMIC_SERVICE;
        Mockito.when(this.rulesVersionService.getRulesVersion(CountryCode.AR)).thenReturn(ruleVersion);
        HomeContent obtainedResult2 = this.mabHomeDataService.callEngine(action2);
        Assert.assertNotNull(obtainedResult2);
        Mockito.verify(this.engineFacade).buildHomeContent(action2);
        Assert.assertTrue(action2.getVersion().equals(RulesVersion.DYNAMIC_SERVICE));
        Assert.assertNull(action2.getForceRulesversion());

    }


}
