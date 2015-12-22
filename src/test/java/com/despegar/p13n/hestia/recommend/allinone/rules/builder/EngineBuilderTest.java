package com.despegar.p13n.hestia.recommend.allinone.rules.builder;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import ar.com.despegar.p13n.hestia.test.MockitoAnnotationBaseTest;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.HomeContent;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.rules.EngineFacade;
import com.despegar.p13n.hestia.recommend.allinone.rules.HomeContentValidator;
import com.despegar.p13n.hestia.recommend.allinone.rules.ItemRuleEngine;
import com.despegar.p13n.hestia.recommend.allinone.rules.LastResortEngine;
import com.despegar.p13n.hestia.recommend.allinone.rules.NoHistoryEngine;
import com.despegar.p13n.hestia.recommend.allinone.rules.RuleContentFiller;
import com.despegar.p13n.hestia.recommend.allinone.rules.RuleContentPriority;
import com.despegar.p13n.hestia.recommend.allinone.rules.RuleSection;
import com.despegar.p13n.hestia.recommend.allinone.rules.SectionFunctionEngine;
import com.despegar.p13n.hestia.recommend.allinone.rules.SectionRuleEngine;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.BaseSectionFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionWrapper;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent.SectionRuleContent;
import com.despegar.p13n.hestia.recommend.allinone.title.TitleEngine;
import com.google.common.collect.Maps;

@RunWith(MockitoJUnitRunner.class)
public class EngineBuilderTest
    extends MockitoAnnotationBaseTest {

    @Mock
    private RuleContentFiller ruleContentFiller;

    @Mock
    private HomeContentValidator validator;

    @Mock
    private TitleEngine titleEngine;

    @Mock
    private EngineFacade engineFacade;

    private SectionFunctionEngine functionEngine;

    private SectionRuleEngine sectionRuleEngine;


    @Mock
    private NoHistoryEngine noHistoryEngine;
    @Mock
    private LastResortEngine lastResortEngine;
    @Mock
    private ItemRuleEngine itemRuleEngine;

    @Before
    public void before() {

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

        Mockito.doNothing().when(this.validator).//
            validate(Matchers.any(Product.class),//
                Matchers.any(HomeContent.class),//
                Matchers.any(ActionRecommendation.class));

        Mockito.doNothing()//
            .when(this.titleEngine).//
            updateTitle(Matchers.any(ActionRecommendation.class),//
                Matchers.any(RuleSection.class),//
                Matchers.any(Offer.class));//

        Map<SectionFunctionCode, SectionFunctionWrapper> map = Maps.newLinkedHashMap();
        map.put(SectionFunctionCode.LAST_RESORT, new SectionFunctionWrapper(new SampleSectionFunction()));

        this.functionEngine = new SectionFunctionEngine(map);

        SectionRuleEngine sectionRuleEngine = new SectionRuleEngine(this.noHistoryEngine, this.ruleContentFiller,
            this.functionEngine, this.titleEngine, this.validator, this.lastResortEngine);
        this.sectionRuleEngine = sectionRuleEngine;
    }

    @Test
    public void test() throws Exception {
        EngineBuilder engineBuilder = new EngineBuilder();
        engineBuilder.setFunctionEngine(this.functionEngine);
        engineBuilder.setSectionRuleEngine(this.sectionRuleEngine);
        engineBuilder.setNoHistoryEngine(this.noHistoryEngine);
        engineBuilder.setLastResortEngine(this.lastResortEngine);
        engineBuilder.setEngineFacade(this.engineFacade);
        engineBuilder.setItemRuleEngine(this.itemRuleEngine);
        engineBuilder.build();
        Mockito.verify(this.engineFacade).start();

    }

    private static class SampleSectionFunction
        extends BaseSectionFunction {

        @Override
        public List<Offer> buildOffers(Product home, Product pr, ActionRecommendation action, Param param) {
            return null;
        }

        @Override
        public RowHome buildRow(Product home, Product pr, ActionRecommendation action, Param param) {
            return null;
        }

        @Override
        public SectionFunctionCode getFunctionCode() {
            return null;
        }

        @Override
        public String getDescription() {
            return null;
        }

    }

}
