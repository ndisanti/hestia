package com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent;

import com.despegar.p13n.hestia.recommend.allinone.rules.RuleSection;
import com.despegar.p13n.hestia.recommend.allinone.rules.SectionsEnum;


public class SectionRuleContent {

    private final RuleSection offer;
    private final RuleSection row1;
    private final RuleSection row2;
    private final RuleSection row3;

    public SectionRuleContent(RuleSection offer, RuleSection row1, RuleSection row2, RuleSection row3) {
        this.offer = offer;
        this.row1 = row1;
        this.row2 = row2;
        this.row3 = row3;
    }

    public RuleSection getOffer() {
        return this.offer;
    }

    public RuleSection getRow1() {
        return this.row1;
    }

    public RuleSection getRow2() {
        return this.row2;
    }

    public RuleSection getRow3() {
        return this.row3;
    }

    public RuleSection get(SectionsEnum section) {
        switch (section) {
        case OFFER:
            return this.getOffer();
        case ROW1:
            return this.getRow1();
        case ROW2:
            return this.getRow2();
        case ROW3:
            return this.getRow3();

        default:
            throw new IllegalArgumentException("Section " + section);
        }
    }

    /**
     * Returns true if it has at least one dynamic section
     */
    public boolean hasDynamicSection() {

        for (SectionsEnum section : SectionsEnum.ORIGINAL_SECTIONS) {
            if (this.get(section).isDynamic()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "SectionRuleContent [offer=" + this.offer + ", row1=" + this.row1 + ", row2=" + this.row2 + ", row3="
            + this.row3 + "]";
    }

    public SectionRuleContent override(SectionsEnum section, RuleSection ruleSection) {

        SectionRuleContent override = null;

        switch (section) {
        case OFFER:
            override = new SectionRuleContent(ruleSection, this.row1, this.row2, this.row3);
            break;
        case ROW1:
            override = new SectionRuleContent(this.offer, ruleSection, this.row2, this.row3);
            break;
        case ROW2:
            override = new SectionRuleContent(this.offer, this.row1, ruleSection, this.row3);
            break;
        case ROW3:
            override = new SectionRuleContent(this.offer, this.row1, this.row2, ruleSection);
            break;
        default:
            throw new IllegalArgumentException("Section " + section);
        }

        return override;
    }

}
