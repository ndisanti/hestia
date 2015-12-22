package com.despegar.p13n.hestia.recommend.allinone.rules.tree.itemcontent;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.rules.CountrySupport;
import com.despegar.p13n.hestia.recommend.allinone.rules.HomeSupport;
import com.despegar.p13n.hestia.recommend.allinone.rules.RuleItem;
import com.despegar.p13n.hestia.recommend.decisiontree.DecisionTree;
import com.despegar.p13n.hestia.recommend.decisiontree.DecisionTree.Path;
import com.despegar.p13n.hestia.recommend.decisiontree.Node.Value;
import com.despegar.p13n.hestia.utils.DumpUtils;
import com.google.common.collect.Lists;

public class DecisionTreeHomeItem {

    protected static final Logger LOG = LoggerFactory.getLogger(DecisionTreeHomeItem.class);

    private DecisionTree<HomeRuleContent> decisionTree = new DecisionTree<HomeRuleContent>(new ActivityTypeNode());

    public void addRule(RuleDefItem rule, HomeRuleContent rc) {
        this.decisionTree.addRule(rule, rc);
    }

    public boolean containsRule(RuleDefItem rule) {
        return this.decisionTree.containsRule(rule);
    }

    public HomeRuleContent getValue(QueryRuleItem query) {
        return this.decisionTree.getValue(query);
    }

    public boolean hasValue(QueryRuleItem query) {
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

        // TODO: llenar

        long end = System.currentTimeMillis();

        LOG.info("Checking ranges took {} ms", (end - init));

    }

    public List<String> dumpAsString(Map<String, String> filterMap, String separator) {

        List<Path<HomeRuleContent>> paths = this.decisionTree.traverse();

        separator = separator == null ? " " : separator;

        final String format = //
        "%-10s" + separator//
            + "%-10s" + separator//
            + "%-10s" + separator//
            + "%-10s" + separator//
            + "%-10s" + separator//
            + "%-20s" + separator//
            + "%-20s" + separator//
            + "%-5s(%-9s/%-9s)";

        List<String> rules = Lists.newArrayList();
        String header = String.format(format, "Activity", "Country", "Home", "Row", "Column", "Id Function",
            "Product Function", "%", "Call", "Full");
        rules.add(header);

        for (Path<HomeRuleContent> path : paths) {

            List<String> p = path.getPath();

            RuleDefItem rdi = new RuleDefItem(//
                ActivityType.valueOf(p.get(0)),//
                CountrySupport.valueOf(p.get(1)), //
                HomeSupport.valueOf(p.get(2)));


            this.printKey(format, rules, rdi, path.getValue(), filterMap);

        }

        return rules;

    }

    // TODO: falta que imprima los otros, rows, hay que revisarlo!
    private void printKey(final String format, List<String> rules, RuleDefItem key, Value<HomeRuleContent> value,
        Map<String, String> filterMap) {

        HomeRuleContent rc = value.getValue();

        String prHome = key.getHome() == null ? "" : DumpUtils.dumpProduct(key.getHome().toString());

        prHome = DumpUtils.dumpProduct(prHome);

        for (int i = 0; i < rc.getOffers().size(); i++) {

            RuleItem ruleItem = rc.getOffers().get(i);

            this.addRuleItem(format, ruleItem, 0, i, rules, key, prHome);
        }
    }

    private void addRuleItem(final String format, RuleItem ruleItem, int row, int column, List<String> rules,
        RuleDefItem key, String prHome) {

        int empty = ruleItem.getUsage().getEmpty().get();
        int full = ruleItem.getUsage().getFull().get();
        int call = empty + full;
        String rate = call == 0 ? " " : String.valueOf((full * 100) / call) + "%";

        rules.add(String.format(format, key.getActivityType(), key.getCountrySupport(), prHome, row == 0 ? "STAR" : row,
            column + 1, ruleItem.getItemIdFunction().toDebug(), ruleItem.getProductFunction(), rate, empty + full, full));
    }

    public List<String> dumpLastResortAsString(Map<String, String> filterMap, String separator) {
        return this.dumpAsString(filterMap, separator);
    }

    public List<Path<HomeRuleContent>> traverse() {
        return this.decisionTree.traverse();
    }
}
