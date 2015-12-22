package com.despegar.p13n.hestia.recommend.allinone.rules.tree.functionpriority;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.recommend.allinone.VisitCity;
import com.despegar.p13n.hestia.recommend.allinone.VisitFlow;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.rules.BuyProductSupport;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Function;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.functionpriority.QueryFunction.QueryFunctionBuilder;
import com.despegar.p13n.hestia.recommend.allinone.title.SectionType;
import com.despegar.p13n.hestia.recommend.decisiontree.DecisionTree;
import com.despegar.p13n.hestia.recommend.decisiontree.DecisionTree.Path;
import com.despegar.p13n.hestia.recommend.decisiontree.Node.Value;
import com.despegar.p13n.hestia.utils.DumpUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class DecisionTreeSectionFunction {

    protected static final Logger LOG = LoggerFactory.getLogger(DecisionTreeSectionFunction.class);

    private DecisionTree<SectionFunctionContent> decisionTree = new DecisionTree<SectionFunctionContent>(new OfferProductNode());


    public void addRule(RuleFunction rule, Function f) {
        this.decisionTree.addRule(rule, new SectionFunctionContent(f));
    }

    public boolean containsRule(RuleFunction rule) {
        return this.decisionTree.containsRule(rule);
    }

    public SectionFunctionContent getValue(QueryFunction query) {
        return this.decisionTree.getValue(query);
    }

    public boolean hasValue(QueryFunction query) {
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

        // TODO:
        long end = System.currentTimeMillis();

        LOG.info("Checking ranges took {} ms", (end - init));

    }

    public List<String> dumpAsString(Map<String, String> filterMap) {

        List<Path<SectionFunctionContent>> paths = this.decisionTree.traverse();

        final String format = "%-10s %-15s %-15s %-10s %-10s %-10s %-10s %-30s %-45s %-5s(%-9s/%-9s)";

        List<String> rules = Lists.newArrayList();
        String header = String.format(format, "Offer", "Activity", "Section", "Buy", "Visit", "City", "Priority",
            "Function", "Param", "%", "Call", "Full");
        rules.add(header);

        for (Path<SectionFunctionContent> path : paths) {

            List<String> p = path.getPath();

            RuleFunction rf = new RuleFunction(//
                Product.valueOf(p.get(0)),//
                ActivityType.valueOf(p.get(1)),//
                SectionType.valueOf(p.get(2)),//
                BuyProductSupport.valueOf(p.get(3)),//
                VisitFlow.valueOf(p.get(4)),//
                VisitCity.valueOf(p.get(5)),//
                FunctionPriority.valueOf(p.get(6)));

            this.printKey(format, rules, rf, path.getValue(), filterMap);

        }

        return rules;
    }


    private void printKey(final String format, List<String> rules, RuleFunction key, Value<SectionFunctionContent> value,
        Map<String, String> filterMap) {

        SectionFunctionContent functionContent = value.getValue();
        Function function = functionContent.getFunction();

        String prOffer = key.getOffer() == null ? "" : key.getOffer().toString();
        String prBought = key.getPrBought() == null ? "" : key.getPrBought().toString();

        prOffer = DumpUtils.dumpProduct(prOffer);
        prBought = DumpUtils.dumpProduct(prBought);

        String functionStr = function.getCode().toString();
        String param = function.getParam().toString();

        ActivityType activity = key.getActivityType();
        SectionType section = key.getSection();
        FunctionPriority priority = key.getPriority();
        SectionFunctionCode code = function.getCode();
        VisitFlow visitFlow = key.getVisitFlow();
        VisitCity visitCity = key.getVisitCity();


        Map<String, String> keyValues = Maps.newHashMap();
        keyValues.put("offer", prOffer.toString());
        keyValues.put("activity", activity.toString());
        keyValues.put("section", section.toString());
        keyValues.put("buy", prBought.toString());
        keyValues.put("visit", visitFlow.toString());
        keyValues.put("city", visitCity.toString());
        keyValues.put("priority", priority.toString());
        keyValues.put("function", code.toString());
        keyValues.put("param", param.toString());

        if (!DumpUtils.shouldAddToDump(filterMap, keyValues)) {
            return;
        }

        prOffer = DumpUtils.dumpProduct(prOffer);

        int empty = functionContent.getUsage().getEmpty().intValue();
        int full = functionContent.getUsage().getFull().intValue();
        int call = empty + full;
        String rate = call == 0 ? " " : String.valueOf((full * 100) / call) + "%";

        String visitCityString = key.getVisitCity().toString();
        String visitCityFormat = visitCityString.substring(0, Math.min(visitCityString.length(), 8));


        rules.add(String.format(format, prOffer, key.getActivityType(), key.getSection(), prBought, key.getVisitFlow(),
            visitCityFormat, key.getPriority(), functionStr, param, rate, empty + full, full));


    }

    public List<Path<SectionFunctionContent>> traverse() {
        return this.decisionTree.traverse();
    }


    public static void main(String[] args) {

        DecisionTreeSectionFunction decisionTree = new DecisionTreeSectionFunction();

        RuleFunction rule = RuleFunction.builder().//
            offer(Product.FLIGHTS). //
            activity(ActivityType.BUY).//
            section(SectionType.OFFER).//
            bought(BuyProductSupport.ANY).//
            visitFlow(VisitFlow.ANY).//
            visitCity(VisitCity.ANY).//
            priority(FunctionPriority.FIRST).//
            build();

        decisionTree.addRule(rule, Function.RANKING_ANY);


        System.out.println("List of paths:" + decisionTree.traverse());

        System.out.println("Decision order: " + decisionTree.dumpRuleDecisionOrder());

        QueryFunction query = QueryFunctionBuilder.create().//
            offer(Product.FLIGHTS).//
            activity(ActivityType.BUY).//
            section(SectionType.OFFER).//
            bought(Product.HOTELS).//
            visitFlow(VisitFlow.ANY).//
            visitCity(VisitCity.ANY).//
            priority(FunctionPriority.FIRST).build();


        System.out.println("Value: " + decisionTree.getValue(query));


        System.out.println("--");

        Map<String, String> filterMap = Maps.newHashMap();
        filterMap.put("offer", "FLIGHTS");

        System.out.println("Dump as string: ");

        for (String s : decisionTree.dumpAsString(filterMap)) {
            System.out.println(s);
        }
    }
}
