package com.despegar.p13n.hestia.recommend.allinone.rules;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.recommend.allinone.Usage;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Function;

/**
 * <p>Encapsulates a rule configuration to build a section.</p>
 */
public class RuleSection {

    public enum RuleSectionType {
        STATIC, // product and function are set
        DYNAMIC_PRODUCT, // produt not set, function is ranking
        DYNAMIC_PRODUCT_FUNCTION, // product and function are not set
        DYNAMIC_FUNCTION;// product is set, function is not set
    };

    private final Product product;
    private final Function function;
    private final Usage usage;

    public RuleSection(Product product, Function function) {
        this.product = product;
        this.function = function;
        this.usage = new Usage();
    }

    public Product getProduct() {
        return this.product;
    }

    public Function getFunction() {
        return this.function;
    }

    public Usage getUsage() {
        return this.usage;
    }

    public boolean isDynamic() {
        return false;
    }

    public RuleSectionType getType() {
        return RuleSectionType.STATIC;
    }

    @Override
    public String toString() {
        return "RuleSection [product=" + this.product + ", function=" + this.function + "]";
    }
}
