package com.despegar.p13n.hestia.recommend.allinone.rules;

import java.util.EnumMap;

import com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent.SectionRuleContent;
import com.google.common.base.Preconditions;

public class RuleContentPriority {

    private EnumMap<SectionsEnum, RuleSectionPriority> bySection = new EnumMap<SectionsEnum, RuleSectionPriority>(
        SectionsEnum.class);

    // original rule content from which this priority is based
    // this is used to update metrics usage in the original rule content
    private SectionRuleContent originalRuleContent;

    public RuleContentPriority(SectionRuleContent ruleContent) {
        this.originalRuleContent = ruleContent;
    }

    public void add(SectionsEnum section, RuleSectionPriority sectionPriority) {
        Preconditions.checkArgument(!this.bySection.containsKey(section));
        this.bySection.put(section, sectionPriority);
    }

    public RuleSectionPriority get(SectionsEnum section) {
        return this.bySection.get(section);
    }

    public SectionRuleContent getOriginalRuleContent() {
        return this.originalRuleContent;
    }

    public void override(SectionsEnum section, RuleSectionPriority sectionPriority) {
        this.bySection.put(section, sectionPriority);
    }

    @Override
    public String toString() {
        return "RuleContentPriority [bySection=" + this.bySection + "]";
    }

    public static RuleContentPriority fromRuleContent(SectionRuleContent ruleContent) {

        RuleContentPriority rcp = new RuleContentPriority(ruleContent);

        for (SectionsEnum section : SectionsEnum.ORIGINAL_SECTIONS) {

            RuleSection ruleSection = ruleContent.get(section);

            RuleSectionPriority rsp = new RuleSectionPriority(ruleSection.getProduct(), ruleSection.getFunction(),
                ruleSection.getUsage());

            rcp.add(section, rsp);
        }

        return rcp;
    }


}
