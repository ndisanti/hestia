package com.despegar.p13n.hestia.recommend.allinone.rules.tree.itemcontent;

import com.despegar.p13n.hestia.recommend.decisiontree.RuleDef;
import com.despegar.p13n.hestia.recommend.decisiontree.ValueNode;
import com.google.common.base.Preconditions;

public class RuleContentNode
    extends ValueNode<HomeRuleContent> {

    @Override
    public Value<HomeRuleContent> add(RuleDef rule, HomeRuleContent ruleContent) {
        Preconditions.checkArgument(this.getValue() == null, "HomeRuleContent already exists for rule " + rule);

        Value<HomeRuleContent> v = new Value<HomeRuleContent>(ruleContent);
        this.setValue(v);
        return v;
    }

}
