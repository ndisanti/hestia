package com.despegar.p13n.hestia.recommend.allinone.rules.builder;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.recommend.allinone.rules.DynamicFunctionRuleSection;
import com.despegar.p13n.hestia.recommend.allinone.rules.DynamicProduct;
import com.despegar.p13n.hestia.recommend.allinone.rules.DynamicProductFunctionRuleSection;
import com.despegar.p13n.hestia.recommend.allinone.rules.DynamicRankingRuleSection;
import com.despegar.p13n.hestia.recommend.allinone.rules.RuleSection;
import com.despegar.p13n.hestia.recommend.allinone.rules.SectionFunctionEngine;
import com.despegar.p13n.hestia.recommend.allinone.rules.SectionsEnum;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Function;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent.SectionRuleContent;
import com.google.common.base.Preconditions;

/**
 * <p>Builds rule content.</p>
 * 
 * <p>Enforce that every section is set and only once.</p>
 */
public class SectionRuleBuilder {

    private RuleSection offer;
    private RuleSection row1;
    private RuleSection row2;
    private RuleSection row3;

    public static SectionRuleBuilder create() {
        return new SectionRuleBuilder();
    }

    public static SectionRuleBuilder copy(SectionRuleContent rc) {
        SectionRuleBuilder rcb = new SectionRuleBuilder();

        for (SectionsEnum section : SectionsEnum.ORIGINAL_SECTIONS) {
            RuleSection ruleSection = rc.get(section);

            switch (ruleSection.getType()) {

            case STATIC:
                rcb.with(section, ruleSection.getProduct(), ruleSection.getFunction());
                break;
            case DYNAMIC_PRODUCT:
                rcb.withDynamicRanking(((DynamicRankingRuleSection) ruleSection).getDynamicProduct(), section);
                break;
            case DYNAMIC_PRODUCT_FUNCTION:
                rcb.withDynamicFunction(((DynamicProductFunctionRuleSection) ruleSection).getDynamicProduct(), section);
                break;
            case DYNAMIC_FUNCTION:
                rcb.withDynamicFunction(((DynamicFunctionRuleSection) ruleSection).getProduct(), section);
                break;
            default:
                throw new IllegalArgumentException("Can't handle " + ruleSection.getType());
            }

        }
        return rcb;
    }

    public SectionRuleBuilder withOffer(Product product, Function function) {
        return this.with(SectionsEnum.OFFER, product, function);
    }

    public SectionRuleBuilder withRow1(Product product, Function function) {
        return this.with(SectionsEnum.ROW1, product, function);
    }

    public SectionRuleBuilder withRow2(Product product, Function function) {
        return this.with(SectionsEnum.ROW2, product, function);
    }

    public SectionRuleBuilder withRow3(Product product, Function function) {
        return this.with(SectionsEnum.ROW3, product, function);
    }

    private SectionRuleBuilder with(SectionsEnum section, Product product, Function function) {
        Preconditions.checkState(this.get(section) == null, section + " was already set");
        this.set(section, new RuleSection(product, function));
        return this;
    }

    // methods to override rule sections

    public SectionRuleBuilder overOffer(Product product, Function function) {
        return this.over(SectionsEnum.OFFER, product, function);
    }

    public SectionRuleBuilder overRow1(Product product, Function function) {
        return this.over(SectionsEnum.ROW1, product, function);
    }

    public SectionRuleBuilder overRow2(Product product, Function function) {
        return this.over(SectionsEnum.ROW2, product, function);
    }

    public SectionRuleBuilder overRow3(Product product, Function function) {
        return this.over(SectionsEnum.ROW3, product, function);
    }

    private SectionRuleBuilder over(SectionsEnum section, Product product, Function function) {
        Preconditions.checkState(this.get(section) != null, section + " was not set");
        this.set(section, new RuleSection(product, function));
        return this;
    }

    // methods for dynamic rules

    /**
     * Product is dynamic, function to be used is {@link RankingDynamicProductFunction}
     */
    public SectionRuleBuilder withDynamicRanking(DynamicProduct dynProduct, SectionsEnum section) {
        Preconditions.checkState(this.get(section) == null, section + " was already set");
        this.set(section, new DynamicRankingRuleSection(dynProduct, Function.RANKING_DYNAMIC_PRODUCT));
        return this;
    }

    /**
     * Product and function are dynamic. Function to be used depends on the {@link SectionFunctionEngine}
     */
    public SectionRuleBuilder withDynamicFunction(DynamicProduct dynProduct, SectionsEnum section) {
        Preconditions.checkState(this.get(section) == null, section + " was already set");
        this.set(section, new DynamicProductFunctionRuleSection(dynProduct));
        return this;
    }

    public SectionRuleBuilder withDynamicFunction(Product pr, SectionsEnum section) {
        Preconditions.checkState(this.get(section) == null, section + " was already set");
        this.set(section, new DynamicFunctionRuleSection(pr));
        return this;
    }

    /**
     * Sets the product for a dynamic rule section
     */
    public SectionRuleBuilder fillDynamicRule(SectionsEnum section, Product product) {

        RuleSection toFill = this.get(section);

        Preconditions.checkState(toFill != null, section + " was not set");
        Preconditions.checkState(toFill.isDynamic(), section + " is not dynamic");

        this.set(section, new RuleSection(product, toFill.getFunction()));
        return this;
    }

    /**
     * Sets the product and function for a dynamic rule section
     */
    public SectionRuleBuilder fillDynamicRule(SectionsEnum section, Product product, Function function) {

        RuleSection toFill = this.get(section);

        Preconditions.checkState(toFill != null, section + " was not set");
        Preconditions.checkState(toFill.isDynamic(), section + " is not dynamic");
        Preconditions.checkState(toFill.getFunction() == null, function + " function was already set");

        this.set(section, new RuleSection(product, function));
        return this;
    }


    public SectionRuleContent build() {
        Preconditions.checkNotNull(this.offer, "offer not set");
        Preconditions.checkNotNull(this.row1, "row1 not set");
        Preconditions.checkNotNull(this.row2, "row2 not set");
        Preconditions.checkNotNull(this.row3, "row3 not set");
        return new SectionRuleContent(this.offer, this.row1, this.row2, this.row3);
    }


    private RuleSection get(SectionsEnum section) {

        switch (section) {
        case OFFER:
            return this.offer;
        case ROW1:
            return this.row1;
        case ROW2:
            return this.row2;
        case ROW3:
            return this.row3;
        default:
            throw new UnsupportedOperationException("Can't handle section enum " + section);
        }
    }


    private void set(SectionsEnum section, RuleSection ruleSection) {

        switch (section) {
        case OFFER:
            this.offer = ruleSection;
            break;
        case ROW1:
            this.row1 = ruleSection;
            break;
        case ROW2:
            this.row2 = ruleSection;
            break;
        case ROW3:
            this.row3 = ruleSection;
            break;
        default:
            throw new UnsupportedOperationException("Can't handle section enum " + section);
        }
    }
}
