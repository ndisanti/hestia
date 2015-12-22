package com.despegar.p13.hestia.recommend.allinone.rules.tree;

import org.junit.Assert;
import org.junit.Test;

import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.activity.Anticipation;
import com.despegar.p13n.hestia.recommend.allinone.activity.LastAction;
import com.despegar.p13n.hestia.recommend.allinone.rules.BuyProductSupport;
import com.despegar.p13n.hestia.recommend.allinone.rules.CountrySupport;
import com.despegar.p13n.hestia.recommend.allinone.rules.HomeSupport;
import com.despegar.p13n.hestia.recommend.allinone.rules.builder.SectionRuleBuilder;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Function;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent.ActivityTypeNode;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent.RuleDefSection;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent.RuleDefSection.RuleBuilder;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent.SectionRuleContent;
import com.despegar.p13n.hestia.recommend.decisiontree.DecisionTree;

public class DecisionTreeTest {

    @Test
    public void testContains() throws Exception {

        DecisionTree<SectionRuleContent> decisionTree = new DecisionTree<SectionRuleContent>(new ActivityTypeNode());

        RuleDefSection rule = RuleBuilder.create().//
            activity(ActivityType.BUY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .home(HomeSupport.ANY)//
            .bought(BuyProductSupport.ANY)//
            .support(CountrySupport.ALL)//
            .build();

        Assert.assertFalse(decisionTree.containsRule(rule));
        decisionTree.addRule(rule, this.buildRuleContent());
        Assert.assertEquals(1, decisionTree.leavesCount());
        Assert.assertTrue(decisionTree.containsRule(rule));

        RuleDefSection ruleLessOneWeek = RuleBuilder.create().//
            activity(ActivityType.BUY)//
            .last(LastAction.LESS_ONE_WEEK)//
            .anticipation(Anticipation.ANY)//
            .home(HomeSupport.ANY)//
            .bought(BuyProductSupport.ANY)//
            .support(CountrySupport.ALL)//
            .build();

        Assert.assertFalse(decisionTree.containsRule(ruleLessOneWeek));
    }


    @Test(expected = IllegalArgumentException.class)
    public void testAddFails() throws Exception {
        DecisionTree<SectionRuleContent> decisionTree = new DecisionTree<SectionRuleContent>(new ActivityTypeNode());

        RuleDefSection rule = RuleBuilder.create().//
            activity(ActivityType.BUY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .home(HomeSupport.ANY)//
            .bought(BuyProductSupport.ANY)//
            .support(CountrySupport.ALL)//
            .build();

        decisionTree.addRule(rule, this.buildRuleContent());
        decisionTree.addRule(rule, this.buildRuleContent());
    }


    private SectionRuleContent buildRuleContent() {
        return SectionRuleBuilder.create()//
            .withOffer(Product.HOTELS, Function.BUY_DESTINATION)//
            .withRow1(Product.HOTELS, Function.RANKING_HOTELS_DESTINATION)//
            .withRow2(Product.FLIGHTS, Function.RECOMMEND.pr1(Product.FLIGHTS).f1(Flow.THANKS).f2(Flow.THANKS))//
            .withRow3(Product.HOTELS, Function.RECOMMEND.pr1(Product.FLIGHTS).f1(Flow.SEARCH).f2(Flow.SEARCH)).build();


    }

}
