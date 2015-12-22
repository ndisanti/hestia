package com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.activity.Anticipation;
import com.despegar.p13n.hestia.recommend.allinone.activity.LastAction;
import com.despegar.p13n.hestia.recommend.allinone.rules.BuyProductSupport;
import com.despegar.p13n.hestia.recommend.allinone.rules.CountrySupport;
import com.despegar.p13n.hestia.recommend.allinone.rules.DynamicFunctionRuleSection;
import com.despegar.p13n.hestia.recommend.allinone.rules.DynamicProductFunctionRuleSection;
import com.despegar.p13n.hestia.recommend.allinone.rules.DynamicRankingRuleSection;
import com.despegar.p13n.hestia.recommend.allinone.rules.HomeSupport;
import com.despegar.p13n.hestia.recommend.allinone.rules.RuleSection;
import com.despegar.p13n.hestia.recommend.allinone.rules.SectionsEnum;
import com.despegar.p13n.hestia.recommend.allinone.rules.RuleSection.RuleSectionType;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent.QueryRuleSection.QueryRuleSectionBuilder;
import com.despegar.p13n.hestia.recommend.decisiontree.DecisionTree;
import com.despegar.p13n.hestia.recommend.decisiontree.DecisionTree.Path;
import com.despegar.p13n.hestia.recommend.decisiontree.Node.Value;
import com.despegar.p13n.hestia.utils.DumpUtils;
import com.despegar.p13n.hestia.utils.ProductCountrySupportUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class DecisionTreeSection {

    protected static final Logger LOG = LoggerFactory.getLogger(DecisionTreeSection.class);

    private DecisionTree<SectionRuleContent> decisionTree = new DecisionTree<SectionRuleContent>(new ActivityTypeNode());

    public void addRule(RuleDefSection rule, SectionRuleContent rc) {
        this.decisionTree.addRule(rule, rc);
    }

    public boolean containsRule(RuleDefSection rule) {
        return this.decisionTree.containsRule(rule);
    }

    public SectionRuleContent getValue(QueryRuleSection query) {
        return this.decisionTree.getValue(query);
    }

    public boolean hasValue(QueryRuleSection query) {
        return this.decisionTree.hasValue(query);
    }

    public String dumpRuleDecisionOrder() {
        return this.decisionTree.dumpRuleDecisionOrder();
    }

    public int leavesCount() {
        return this.decisionTree.leavesCount();
    }

    public void checkRanges() {

        long init = System.currentTimeMillis();

        for (ActivityType activity : ActivityType.BIZ_TYPES) {
            for (CountryCode cc : CountrySupport.RELEVANTS_CC) {
                for (CountrySupport cs : EnumSet.of(CountrySupport.ALL, CountrySupport.SOME)) {
                    for (int lastActionDay = 0; lastActionDay < 90; lastActionDay++) {
                        for (int anticipationDay = 0; anticipationDay < 90; anticipationDay++) {
                            for (Product home : ProductCountrySupportUtils.getHomesSupported(cc)) {

                                QueryRuleSection query = QueryRuleSectionBuilder.create()//
                                    .activity(activity)//
                                    .lastAction(lastActionDay)//
                                    .anticipation(anticipationDay)//
                                    .bought(activity == ActivityType.BUY ? Product.HOTELS : null)//
                                    .home(home)//
                                    .support(cs)//
                                    .build();


                                SectionRuleContent content = this.getValue(query);

                                if (content == null) {
                                    String msg = String.format("Rule not found for query: %s", query);

                                    throw new IllegalStateException(msg);
                                }

                            }
                        }
                    }
                }
            }
        }

        long end = System.currentTimeMillis();

        LOG.info("Checking ranges took {} ms", (end - init));

    }

    public List<String> dumpAsString(Map<String, String> filterMap, String separator) {

        List<Path<SectionRuleContent>> paths = this.decisionTree.traverse();

        separator = separator == null ? " " : separator;

        final String format = "%-10s" + separator//
            + "%-15s" + separator//
            + "%-15s" + separator//
            + "%-10s" + separator//
            + "%-10s" + separator//
            + "%-10s" + separator//
            + "%-8s" + separator//
            + "%-10s" + separator//
            + "%-30s" + separator//
            + "%-50s" + separator//
            + "%-5s(%-9s/%-9s)";

        List<String> rules = Lists.newArrayList();
        String header = String.format(format, "Activity", "LastAction", "Anticipation", "Buy", "Home", "Country", "Section",
            "Offer", "Function", "Param", "%", "Call", "Full");
        rules.add(header);

        for (Path<SectionRuleContent> path : paths) {

            List<String> p = path.getPath();

            RuleDefSection rpk = new RuleDefSection(ActivityType.valueOf(p.get(0)),//
                LastAction.valueOf(p.get(3)),//
                Anticipation.valueOf(p.get(4)),//
                BuyProductSupport.valueOf(p.get(2)),//
                HomeSupport.valueOf(p.get(1)),//
                CountrySupport.valueOf(p.get(5)));//

            this.printKey(format, rules, rpk, path.getValue(), filterMap);

        }

        return rules;
    }

    public List<String> dumpLastResortAsString(Map<String, String> filterMap, String separator) {
        filterMap.put("rulesVersion", "ANY");
        return this.dumpAsString(filterMap, separator);
    }

    private void printKey(final String format, List<String> rules, RuleDefSection key, Value<SectionRuleContent> value,
        Map<String, String> filterMap) {

        SectionRuleContent rc = value.getValue();

        String prBought = key.getPrBought() == null ? "" : DumpUtils.dumpProduct(key.getPrBought().toString());
        String prHome = key.getHome() == null ? "" : DumpUtils.dumpProduct(key.getHome().toString());


        prBought = DumpUtils.dumpProduct(prBought);
        prHome = DumpUtils.dumpProduct(prHome);



        for (SectionsEnum section : SectionsEnum.ORIGINAL_SECTIONS) {
            RuleSection ruleSection = rc.get(section);
            String function;
            String param;

            if (ruleSection.getType() == RuleSectionType.DYNAMIC_PRODUCT_FUNCTION
                || ruleSection.getType() == RuleSectionType.DYNAMIC_FUNCTION) {
                function = "<DYNAMIC>";
                param = "<DYNAMIC>";
            } else {
                function = ruleSection.getFunction().getCode().toString();
                param = ruleSection.getFunction().getParam().toString();
            }

            String offer;


            switch (ruleSection.getType()) {
            case DYNAMIC_PRODUCT_FUNCTION:
                offer = "<" + ((DynamicProductFunctionRuleSection) ruleSection).getDynamicProduct().toString() + ">";
                break;
            case DYNAMIC_FUNCTION:
                offer = "<" + ((DynamicFunctionRuleSection) ruleSection).getProduct().toString() + ">";
                break;
            case DYNAMIC_PRODUCT:
                offer = "<" + ((DynamicRankingRuleSection) ruleSection).getDynamicProduct().toString() + ">";
                break;
            case STATIC:
                offer = ruleSection.getProduct().toString();
                break;
            default:
                throw new IllegalArgumentException("Cant handle " + ruleSection.getType());
            }

            Map<String, String> keyValues = Maps.newHashMap();
            keyValues.put("activity", key.getActivityType().toString());
            keyValues.put("lastAction", key.getLastAction().toString());
            keyValues.put("anticipation", key.getAnticipation().toString());
            keyValues.put("buy", key.getPrBought().toString());
            keyValues.put("home", key.getHome().toString());
            keyValues.put("country", key.getCountrySupport().toString());
            keyValues.put("section", section.toString());
            keyValues.put("offer", offer.toString());
            keyValues.put("function", function.toString());
            keyValues.put("param", param.toString());

            if (!DumpUtils.shouldAddToDump(filterMap, keyValues)) {
                continue;
            }

            offer = DumpUtils.dumpProduct(offer);

            int empty = ruleSection.getUsage().getEmpty().get();
            int full = ruleSection.getUsage().getFull().get();
            int call = empty + full;
            String rate = call == 0 ? " " : String.valueOf((full * 100) / call) + "%";

            rules.add(String.format(format, key.getActivityType(), key.getLastAction().getDesc(), key.getAnticipation()
                .getDesc(), prBought, prHome, key.getCountrySupport(), section, offer, function, param, rate, empty + full,
                full));
        }

    }

    public List<Path<SectionRuleContent>> traverse() {
        return this.decisionTree.traverse();
    }


}
