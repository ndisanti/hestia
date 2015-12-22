package com.despegar.p13n.hestia.recommend.allinone.rules;

import com.despegar.p13n.euler.commons.client.model.Product;


/**
 * <p>{@link RuleSection} that its function is set at runtime</p>
 */
public class DynamicFunctionRuleSection
    extends RuleSection {


    public DynamicFunctionRuleSection(Product pr) {
        super(pr, null);
    }

    @Override
    public boolean isDynamic() {
        return true;
    }

    @Override
    public RuleSectionType getType() {
        return RuleSectionType.DYNAMIC_FUNCTION;
    }

    @Override
    public String toString() {
        return "DynamicFunctionRuleSection [pr=" + this.getProduct() + "]";
    }

}
