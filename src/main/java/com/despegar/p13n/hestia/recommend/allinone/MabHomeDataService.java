package com.despegar.p13n.hestia.recommend.allinone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.HomeUtils;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.HomeContent;
import com.despegar.p13n.hestia.api.data.model.RulesVersion;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.rules.EngineFacade;
import com.despegar.p13n.hestia.service.MabHomeService;
import com.despegar.p13n.hestia.service.MultiObjectVersionService;
import com.despegar.p13n.mab.ArmCallable;
import com.despegar.p13n.mab.ArmMapping;
import com.despegar.p13n.mab.ConversionFlow;
import com.despegar.p13n.mab.ConversionProduct;
import com.despegar.p13n.mab.MabService;
import com.despegar.p13n.mab.MabTest;
import com.despegar.p13n.mab.MabTestConfig;
import com.despegar.p13n.mab.Profile;
import com.despegar.p13n.mab.distribution.ThompsonSamplingDistribution;
import com.newrelic.api.agent.Trace;

@Service
public class MabHomeDataService
    implements MabHomeService {

    private MabService mabService;
    private EngineFacade engineFacade;
    private MabTest<ActionRecommendation, HomeContent> rulesMabTest;
    private MabTest<ActionRecommendation, MultiObjecHomeVersion> multiObjectMabTest;
    private RulesVersionService rulesVersionService;
    private MultiObjectVersionService multiObjectService;

    @Autowired
    public MabHomeDataService(MabService mabService, EngineFacade engineFacade, RulesVersionService rulesVersionService,
        MultiObjectVersionService multiObjectService) {
        super();
        this.mabService = mabService;
        this.engineFacade = engineFacade;
        this.rulesVersionService = rulesVersionService;
        this.multiObjectService = multiObjectService;
        this.rulesMabTest = this.getRulesMabTest();
        this.multiObjectMabTest = this.getMultiObjectMabTest();
    }

    private MabTest<ActionRecommendation, MultiObjecHomeVersion> getMultiObjectMabTest() {
        ArmMapping<ActionRecommendation, MultiObjecHomeVersion> armMapping = this.getMultiObjectArmMapping();
        return this.createMOTest(armMapping, "MultiObjectTest", "2015-05-09");
    }

    private MabTest<ActionRecommendation, MultiObjecHomeVersion> createMOTest(
        ArmMapping<ActionRecommendation, MultiObjecHomeVersion> armMapping, String testName, String startDate) {
        MabTestConfig<ActionRecommendation, MultiObjecHomeVersion> config = this.getMabMOConfigTest(armMapping, testName,
            startDate);
        return this.mabService.createTest(config);
    }

    private MabTestConfig<ActionRecommendation, MultiObjecHomeVersion> getMabMOConfigTest(
        ArmMapping<ActionRecommendation, MultiObjecHomeVersion> armMapping, String testName, String startDate) {
        return MabTestConfig.<ActionRecommendation, MultiObjecHomeVersion> builder()//
            .name(HomeUtils.APP_HOME_NAME, testName)//
            .startDate(startDate)//
            .duration(90)//
            .confidenceInterval(5)//
            .rebalancerGraceTime(0)//
            .armMapping(armMapping)//
            .rebalancer(new ThompsonSamplingDistribution())//
            .conversionProduct(ConversionProduct.TOTAL)//
            .conversionFlow(ConversionFlow.THANKS)//
            .noDuration(true)//
            .build();
    }

    public MabTest<ActionRecommendation, HomeContent> getRulesMabTest() {
        ArmMapping<ActionRecommendation, HomeContent> armMapping = this.getAllRulesArmMapping();
        return this.createTest(armMapping, "RulesVersionsTest", "2015-05-09");
    }

    private MabTest<ActionRecommendation, HomeContent> createTest(ArmMapping<ActionRecommendation, HomeContent> armMapping,
        String testName, String startDate) {
        MabTestConfig<ActionRecommendation, HomeContent> config = this.getMabConfigTest(armMapping, testName, startDate);
        return this.mabService.createTest(config);
    }

    private MabTestConfig<ActionRecommendation, HomeContent> getMabConfigTest(
        ArmMapping<ActionRecommendation, HomeContent> armMapping, String testName, String startDate) {
        return MabTestConfig.<ActionRecommendation, HomeContent> builder()//
            .name(HomeUtils.APP_HOME_NAME, testName)//
            .startDate(startDate)//
            .duration(90)//
            .confidenceInterval(5)//
            .rebalancerGraceTime(0)//
            .armMapping(armMapping)//
            .rebalancer(new ThompsonSamplingDistribution())//
            .conversionProduct(ConversionProduct.TOTAL)//
            .conversionFlow(ConversionFlow.THANKS)//
            .noDuration(true)//
            .build();
    }

    private ArmMapping<ActionRecommendation, MultiObjecHomeVersion> getMultiObjectArmMapping() {
        return ArmMapping.<ActionRecommendation, MultiObjecHomeVersion> builder().//
            addArm("V1GROUPED", 33, new ArmCallable<ActionRecommendation, MultiObjecHomeVersion>() {

                public MultiObjecHomeVersion call(ActionRecommendation action) {
                    return MabHomeDataService.this.manageForceMOVersion(action, MultiObjecHomeVersion.V1_GROUPED);
                }
            }).addArm("V1INTERLIVED", 34, new ArmCallable<ActionRecommendation, MultiObjecHomeVersion>() {

                public MultiObjecHomeVersion call(ActionRecommendation action) {
                    return MabHomeDataService.this.manageForceMOVersion(action, MultiObjecHomeVersion.V1_INTERLIVED);
                }
            }).addArm("V4ROW4", 33, new ArmCallable<ActionRecommendation, MultiObjecHomeVersion>() {

                public MultiObjecHomeVersion call(ActionRecommendation action) {
                    return MabHomeDataService.this.manageForceMOVersion(action, MultiObjecHomeVersion.V4_ROW4);
                }
            }).defaultArm(new ArmCallable<ActionRecommendation, MultiObjecHomeVersion>() {

                public MultiObjecHomeVersion call(ActionRecommendation action) {
                    return MabHomeDataService.this.manageForceMOVersion(action, MultiObjecHomeVersion.V4_ROW4);
                }
            }).build();

    }

    private ArmMapping<ActionRecommendation, HomeContent> getAllRulesArmMapping() {

        return ArmMapping.<ActionRecommendation, HomeContent> builder().//
            addArm("dynamicService", 33, new ArmCallable<ActionRecommendation, HomeContent>() {

                public HomeContent call(ActionRecommendation action) {
                    return MabHomeDataService.this.callForDynamicServiceRulesVersion(action);
                }
            }).addArm("monoDestination", 34, new ArmCallable<ActionRecommendation, HomeContent>() {

                public HomeContent call(ActionRecommendation action) {
                    return MabHomeDataService.this.callForMonoDestinationRulesVersion(action);
                }
            }).addArm("multiDestination", 33, new ArmCallable<ActionRecommendation, HomeContent>() {

                public HomeContent call(ActionRecommendation action) {
                    return MabHomeDataService.this.callForMultiDestinationRulesVersion(action);
                }
            }).defaultArm(new ArmCallable<ActionRecommendation, HomeContent>() {

                public HomeContent call(ActionRecommendation action) {
                    return MabHomeDataService.this.callDefault(action);
                }
            }).build();
    }

    protected HomeContent callForMonoDestinationRulesVersion(ActionRecommendation action) {
        this.manageForceRulesVersion(action, RulesVersion.MONO_DESTINATION);
        action.addDebug("Mab: MONO_DESTINATION");
        action.setMabTrace("MONO_DESTINATION");
        return this.buildHomeContent(action);
    }

    protected HomeContent callForMultiDestinationRulesVersion(ActionRecommendation action) {
        this.manageForceRulesVersion(action, RulesVersion.MULTI_DESTINATION);
        action.addDebug("Mab: MULTI_DESTINATION");
        action.setMabTrace("MULTI_DESTINATION");
        return this.buildHomeContent(action);
    }

    protected HomeContent callForDynamicServiceRulesVersion(ActionRecommendation action) {

        this.manageForceRulesVersion(action, RulesVersion.DYNAMIC_SERVICE);
        action.addDebug("Mab: DYNAMIC_SERVICE");
        action.setMabTrace("DYNAMIC_SERVICE");
        return this.buildHomeContent(action);
    }

    private HomeContent callDefault(ActionRecommendation action) {
        RulesVersion version = MabHomeDataService.this.getRulesVersion(action.getCountryCode());
        this.manageForceRulesVersion(action, version);
        action.addDebug("Mab: default");
        action.setMabTrace("default");
        return this.buildHomeContent(action);
    }

    private void manageForceRulesVersion(ActionRecommendation action, RulesVersion rulesVersion) {
        RulesVersion forceVersion = action.getForceRulesversion();
        if (forceVersion != null) {
            action.setVersion(forceVersion);
        } else {
            action.setVersion(rulesVersion);
        }
    }


    private MultiObjecHomeVersion manageForceMOVersion(ActionRecommendation action, MultiObjecHomeVersion moVersion) {
        action.addDebug("Mab: " + moVersion);
        return action.getForceHomeVersion() == null ? moVersion : action.getForceHomeVersion();
    }

    @Trace
    private HomeContent buildHomeContent(ActionRecommendation action) {
        action.addtoDebug();
        action.addtoTrace();
        HomeContent homeContent = this.engineFacade.buildHomeContent(action);
        MultiObjecHomeVersion homeVersion = action.getForceHomeVersion();
        this.multiObjectService.addObjectsInOffers(homeContent, action);
        this.multiObjectService.addObjectsInRows(homeContent, action, homeVersion);
        return homeContent;
    }

    private RulesVersion getRulesVersion(CountryCode countryCode) {
        return this.rulesVersionService.getRulesVersion(countryCode);
    }

    @Override
    public HomeContent callEngine(ActionRecommendation action) {
        HomeContent homeContent = null;
        boolean forceDefaultArm = this.isDefaultARM(action);
        Profile profile = Profile.create("cc", action.getCountryCode().name())//
            .add("buy", String.valueOf(action.getActivityType() == ActivityType.BUY));

        MultiObjecHomeVersion moVersion = this.multiObjectMabTest.call(action, action.getUserId(), profile, forceDefaultArm);
        action.setForceHomeVersion(moVersion);
        action.setNoHistoryRecommendedHome(Product.HOTELS);

        if (CountryCode.isInternational(action.getCountryCode())) {
            homeContent = this.callDefault(action);
        } else {
            homeContent = this.rulesMabTest.call(action, action.getUserId(), profile, forceDefaultArm);
        }
        return homeContent;
    }

    private boolean isDefaultARM(ActionRecommendation action) {
        return action.getCountryCode() == CountryCode.PE;
    }

}
