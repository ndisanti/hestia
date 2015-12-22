package com.despegar.p13n.hestia.recommend.allinone.rules.tree.functionpriority;

import com.despegar.p13n.hestia.recommend.decisiontree.RuleDef;
import com.despegar.p13n.hestia.recommend.decisiontree.ValueNode;
import com.google.common.base.Preconditions;

public class FunctionNode
    extends ValueNode<SectionFunctionContent> {

    @Override
    public Value<SectionFunctionContent> add(RuleDef rule, SectionFunctionContent function) {
        Preconditions.checkArgument(this.getValue() == null, "Function already exists for rule " + rule);

        Value<SectionFunctionContent> v = new Value<SectionFunctionContent>(function);
        this.setValue(v);
        return v;
    }

}
