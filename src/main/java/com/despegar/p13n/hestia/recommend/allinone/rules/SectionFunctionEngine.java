package com.despegar.p13n.hestia.recommend.allinone.rules;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.HomeProduct;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Function;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionWrapper;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.functionpriority.DecisionTreeSectionFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.functionpriority.QueryFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.functionpriority.RuleFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.functionpriority.SectionFunctionContent;
import com.despegar.p13n.hestia.recommend.allinone.title.TitleData;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class SectionFunctionEngine {

    /**
     * Decision tree with rules to define which function should be apply for dynamic service
     */
    private DecisionTreeSectionFunction decisionTree = new DecisionTreeSectionFunction();

    private Map<SectionFunctionCode, SectionFunctionWrapper> functions;

    public void add(RuleFunction rule, Function f) {
        this.decisionTree.addRule(rule, f);
    }

    public SectionFunctionContent query(QueryFunction query) {
        return this.decisionTree.getValue(query);
    }

    public boolean hasRuleDefined(QueryFunction query) {
        return this.decisionTree.hasValue(query);
    }

    public SectionFunctionEngine(Map<SectionFunctionCode, SectionFunctionWrapper> functions) {
        this.functions = functions;
    }

    public SectionFunction getSectionFunction(Function function) {
        SectionFunctionWrapper functionWrapper = this.functions.get(function.getCode());
        Preconditions.checkNotNull(functionWrapper, "Missing function %s ", function);
        return functionWrapper;
    }

    public List<Offer> buildOffers(Function function, Product home, Product pr, ActionRecommendation action) {
        Preconditions.checkNotNull(function);
        Preconditions.checkNotNull(function.getCode());
        Preconditions.checkNotNull(pr);
        Preconditions.checkNotNull(action);

        action.setTitleData(new TitleData());
        action.setCurrentSection(SectionsEnum.OFFER);

        final SectionFunction sectionFunction = this.getSectionFunction(function);
        Param param = Param.copy(function.getParam()); // copy to avoid changes
        return sectionFunction.buildOffers(home, pr, action, param);
    }

    public RowHome buildRow(Function function, Product home, Product pr, ActionRecommendation action) {
        Preconditions.checkNotNull(function);
        Preconditions.checkNotNull(function.getCode());
        Preconditions.checkNotNull(pr);
        Preconditions.checkNotNull(action);

        final SectionFunction sectionFunction = this.getSectionFunction(function);
        Param param = Param.copy(function.getParam()); // copy to avoid changes
        return sectionFunction.buildRow(home, pr, action, param);
    }

    public HomeProduct buildHomeProduct(Function function, Product pr, ActionRecommendation action) {
        // we need a product for the home because of the function validations
        Product home = Product.HOME_AS_PRODUCT;

        try {
            Preconditions.checkNotNull(function);
            Preconditions.checkNotNull(function.getCode());
            Preconditions.checkNotNull(pr);
            Preconditions.checkNotNull(action);

            List<Offer> offers = this.buildOffers(function, home, pr, action);

            List<RowHome> rows = Lists.newArrayList();

            for (SectionsEnum section : EnumSet.of(SectionsEnum.ROW1, SectionsEnum.ROW2, SectionsEnum.ROW3)) {
                action.setCurrentSection(section);
                action.setTitleData(new TitleData());
                rows.add(this.buildRow(function, home, pr, action));
            }

            HomeProduct homeProduct = new HomeProduct(offers, rows);
            return homeProduct;

        } catch (Exception e) {
            action.addDebug("Exception: " + e);
            if (e.getStackTrace() != null) {
                String stackTrace = ExceptionUtils.getStackTrace(e);
                action.addDebug("Stacktrace: " + stackTrace);
            }
            return null;
        }
    }

    public Collection<SectionFunctionWrapper> allFunctionWrappers() {
        return this.functions.values();
    }

    public List<String> dumpFunctionDescriptions() {

        List<String> list = Lists.newArrayList();

        for (SectionFunctionWrapper functionWrapper : this.allFunctionWrappers()) {
            list.add(functionWrapper.getFunctionCode() + ": " + functionWrapper.getFunction().getDescription());
        }

        return list;
    }

    public List<String> dumpRules(Map<String, String> filterMap) {
        return this.decisionTree.dumpAsString(filterMap);
    }

}
