package com.despegar.p13n.hestia.recommend.allinone.rules;


/**
 * <p>{@link RuleSection} that its product and function are set at runtime</p>
 */
public class DynamicProductFunctionRuleSection
    extends RuleSection {

    private DynamicProduct dynamicProduct;

    public DynamicProductFunctionRuleSection(DynamicProduct dynamicProduct) {
        super(null, null);
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
        return RuleSectionType.DYNAMIC_PRODUCT_FUNCTION;
    }

    @Override
    public String toString() {
        return "DynamicProductFunctionRuleSection [dynamicProduct=" + this.dynamicProduct + "]";
    }

}
