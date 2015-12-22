package com.despegar.p13n.hestia.recommend.allinone.rules.tree.itemcontent;

import java.net.UnknownHostException;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.rules.CountrySupport;
import com.despegar.p13n.hestia.recommend.allinone.rules.HomeSupport;
import com.despegar.p13n.hestia.recommend.allinone.rules.builder.HomeRuleBuilder;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.ProductFuncCode;
import com.despegar.p13n.hestia.recommend.allinone.rules.item.IdFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.itemcontent.QueryRuleItem.QueryRuleItemBuilder;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.itemcontent.RuleDefItem.RuleBuilder;

public class DecisionTreeSample {

    public static void main(String[] args) throws UnknownHostException {


        HomeRuleContent hrc = HomeRuleBuilder.create()//
            .withOffers(IdFunction.SEARCH_DESTINATIONS.index(1), ProductFuncCode.PRODUCTS)//
            .withRow1(IdFunction.SEARCH_DESTINATIONS.index(2), ProductFuncCode.PRODUCTS)//
            .withRow2(IdFunction.SEARCH_DESTINATIONS.index(3), ProductFuncCode.PRODUCTS)//
            .withRow3(IdFunction.SEARCH_DESTINATIONS, ProductFuncCode.PRODUCTS)//
            .withItem(1, 2, IdFunction.SEARCH_DESTINATIONS, ProductFuncCode.PRODUCTS)//
            .build();

        DecisionTreeHomeItem decisionTree = new DecisionTreeHomeItem();

        RuleDefItem rule1 = RuleBuilder.create().//
            activity(ActivityType.BUY)//
            .support(CountrySupport.ALL)//
            .home(HomeSupport.FLIGHTS).build();

        RuleDefItem rule2 = RuleBuilder.create().//
            activity(ActivityType.BUY)//
            .support(CountrySupport.SOME)//
            .home(HomeSupport.CARS).build();

        decisionTree.addRule(rule1, hrc);
        decisionTree.addRule(rule2, hrc);

        System.out.println("Number of rules: " + decisionTree.leavesCount());

        QueryRuleItem query = QueryRuleItemBuilder.create().//
            activity(ActivityType.BUY).//
            support(CountrySupport.ALL).//
            home(Product.FLIGHTS).build();


        System.out.println("List of paths:" + decisionTree.traverse());

        System.out.println("Decision order: " + decisionTree.dumpRuleDecisionOrder());

        System.out.println("Value: " + decisionTree.getValue(query));

        System.out.println("--");

    }
}
