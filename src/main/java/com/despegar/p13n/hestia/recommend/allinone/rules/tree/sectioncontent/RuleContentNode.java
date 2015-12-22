package com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent;

import com.despegar.p13n.hestia.recommend.decisiontree.RuleDef;
import com.despegar.p13n.hestia.recommend.decisiontree.ValueNode;
import com.google.common.base.Preconditions;

public class RuleContentNode
    extends ValueNode<SectionRuleContent> {

    @Override
    public Value<SectionRuleContent> add(RuleDef rule, SectionRuleContent ruleContent) {
        Preconditions.checkArgument(this.getValue() == null, "RuleContent already exists for rule " + rule);

        Value<SectionRuleContent> v = new Value<SectionRuleContent>(ruleContent);
        this.setValue(v);
        return v;
    }

}
