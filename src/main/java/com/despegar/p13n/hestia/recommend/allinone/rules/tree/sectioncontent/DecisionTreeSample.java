package com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent;

import java.net.UnknownHostException;

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
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent.QueryRuleSection.QueryRuleSectionBuilder;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent.RuleDefSection.RuleBuilder;

public class DecisionTreeSample {

    public static void main(String[] args) throws UnknownHostException {

        SectionRuleContent rc = SectionRuleBuilder.create()//
            .withOffer(Product.HOTELS, Function.BUY_DESTINATION)//
            .withRow1(Product.HOTELS, Function.RANKING_HOTELS_DESTINATION)//
            .withRow2(Product.FLIGHTS, Function.RECOMMEND.pr1(Product.FLIGHTS).f1(Flow.THANKS).f2(Flow.THANKS))//
            .withRow3(Product.HOTELS, Function.RECOMMEND.pr1(Product.FLIGHTS).f1(Flow.THANKS).f2(Flow.SEARCH)).build();

        DecisionTreeSection decisionTree = new DecisionTreeSection();


        RuleDefSection rule1 = RuleBuilder.create().//
            activity(ActivityType.BUY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .home(HomeSupport.ANY)//
            .bought(BuyProductSupport.ANY)//
            .support(CountrySupport.ALL)//
            .build();

        RuleDefSection rule2 = RuleBuilder.create().//
            activity(ActivityType.BUY)//
            .last(LastAction.ANY)//
            .anticipation(Anticipation.ANY)//
            .home(HomeSupport.ANY)//
            .bought(BuyProductSupport.ANY)//
            .support(CountrySupport.SOME)//
            .build();
        decisionTree.addRule(rule1, rc);
        decisionTree.addRule(rule2, rc);

        System.out.println("Number of rules: " + decisionTree.leavesCount());

        QueryRuleSection query = QueryRuleSectionBuilder.create()//
            .activity(ActivityType.BUY)//
            .anticipation(0)//
            .lastAction(0)//
            .home(Product.CARS)//
            .bought(Product.HOTELS)//
            .support(CountrySupport.ALL)//
            .build();


        System.out.println("List of paths:" + decisionTree.traverse());

        System.out.println("Decision order: " + decisionTree.dumpRuleDecisionOrder());

        System.out.println("Value: " + decisionTree.getValue(query));

        System.out.println("--");
    }
}
