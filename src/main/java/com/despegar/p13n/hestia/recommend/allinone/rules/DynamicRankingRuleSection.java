package com.despegar.p13n.hestia.recommend.allinone.rules;

import com.despegar.p13n.hestia.recommend.allinone.rules.section.Function;

/**
 * <p>{@link RuleSection} that its product is set at runtime</p>
 */
public class DynamicRankingRuleSection
    extends RuleSection {

    private DynamicProduct dynamicProduct;

    public DynamicRankingRuleSection(DynamicProduct dynamicProduct, Function function) {
        super(null, function);
        this.dynamicProduct = dynamicProduct;
    }

    public DynamicProduct getDynamicProduct() {
        return this.dynamicProduct;
    }

    @Override
    public boolean isDynamic() {
        return true;
    }

    @Override
    public RuleSectionType getType() {
        return RuleSectionType.DYNAMIC_PRODUCT;
    }

    @Override
    public String toString() {
        return "DynamicRankingRuleSection [dynamicProduct=" + this.dynamicProduct + ", getFunction()=" + this.getFunction()
            + "]";
    }

}
