package com.despegar.p13n.hestia.recommend.allinone.rules;

import java.util.List;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.recommend.allinone.Usage;
import com.despegar.p13n.hestia.recommend.allinone.rules.RuleSection.RuleSectionType;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Function;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.functionpriority.SectionFunctionContent;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * <p>Encapsulates a rule configuration to build a section.</p>
 */
public class RuleSectionPriority {

    private final Product product;
    private final List<SectionFunctionContent> functions;
    private final Usage usage;

    public RuleSectionPriority(Product product, List<SectionFunctionContent> functions, Usage usage) {
        Preconditions.checkNotNull(product);
        Preconditions.checkNotNull(functions);
        Preconditions.checkNotNull(usage);
        this.product = product;
        this.functions = functions;
        this.usage = usage;
    }

    public RuleSectionPriority(Product product, Function function, Usage usage) {
        this(product, Lists.newArrayList(new SectionFunctionContent(function)), usage);
    }

    public Product getProduct() {
        return this.product;
    }

    public List<SectionFunctionContent> getFunctions() {
        return this.functions;
    }

    public Usage getUsage() {
        return this.usage;
    }

    public RuleSectionType getType() {
        return RuleSectionType.STATIC;
    }

    @Override
    public String toString() {
        return "RuleSectionPriority [product=" + this.product + ", functions=" + this.functions + "]";
    }
}
