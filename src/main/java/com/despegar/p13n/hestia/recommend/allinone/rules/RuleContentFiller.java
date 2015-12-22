package com.despegar.p13n.hestia.recommend.allinone.rules;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.despegar.framework.lang.Pair;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.data.FlightData;
import com.despegar.p13n.euler.commons.client.model.data.RouteType;
import com.despegar.p13n.hestia.client.BuyMatrixCounterClient;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.VisitCity;
import com.despegar.p13n.hestia.recommend.allinone.VisitFlow;
import com.despegar.p13n.hestia.recommend.allinone.activity.BuyActivity;
import com.despegar.p13n.hestia.recommend.allinone.activity.UserActivity;
import com.despegar.p13n.hestia.recommend.allinone.matrix.AnticipationMatrix;
import com.despegar.p13n.hestia.recommend.allinone.matrix.LastActionMatrix;
import com.despegar.p13n.hestia.recommend.allinone.matrix.MatrixKey;
import com.despegar.p13n.hestia.recommend.allinone.matrix.ProductMetricCount;
import com.despegar.p13n.hestia.recommend.allinone.matrix.SearchCount;
import com.despegar.p13n.hestia.recommend.allinone.rules.builder.SectionRuleBuilder;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Function;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.functionpriority.FunctionPriority;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.functionpriority.QueryFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.functionpriority.QueryFunction.QueryFunctionBuilder;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.functionpriority.SectionFunctionContent;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent.SectionRuleContent;
import com.despegar.p13n.hestia.recommend.allinone.title.SectionType;
import com.despegar.p13n.hestia.utils.ProductCountrySupportUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.newrelic.api.agent.Trace;

/**
 * <p>For dynamic rule sections it fills the product based on the buy probability matrix
 * and the function based on the {@link SectionFunctionEngine}</p>
 *
 * <p>For static rule sections it does nothing</p>
 *
 * <p>Product to offer for ROW1, ROW2 and ROW3 can't be the same</p>
 */
@Service
public class RuleContentFiller {

    protected static final Logger LOG = LoggerFactory.getLogger(RuleContentFiller.class);

    @Autowired
    private BuyMatrixCounterClient buyMatrixService;

    @Autowired
    private SectionFunctionEngine functionEngine;

    @Trace
    public RuleContentPriority buildRuleContent(SectionRuleContent ruleContent, ActionRecommendation action) {

        action.addDebug(ruleContent);

        RuleContentPriority ruleContentPriority = null;

        if (ruleContent.hasDynamicSection()) {
            ruleContentPriority = this.buildConcreteRuleContent(action, ruleContent);
        } else {

            ruleContentPriority = RuleContentPriority.fromRuleContent(ruleContent);
        }

        return ruleContentPriority;
    }

    private EnumSet<SectionsEnum> getSectionsToBeFilled(SectionRuleContent ruleContent) {

        EnumSet<SectionsEnum> sectionsToFill = EnumSet.noneOf(SectionsEnum.class);

        for (SectionsEnum section : SectionsEnum.ORIGINAL_SECTIONS) {
            if (ruleContent.get(section).isDynamic()) {
                sectionsToFill.add(section);
            }
        }

        return sectionsToFill;
    }

    private RuleContentPriority buildConcreteRuleContent(ActionRecommendation action, SectionRuleContent ruleContent) {

        EnumSet<SectionsEnum> sectionsToFill = this.getSectionsToBeFilled(ruleContent);
        action.addDebug("Dynamic sections: " + sectionsToFill);

        MatrixKey key = null;
        Product lastSearchProduct = null;
        switch (action.getActivityType()) {
        case NO_HISTORY: {
            key = this.getMatrixKeyForNoHistory(action);
            break;
        }
        case SEARCH: {
            key = this.getMatrixKeyForSearch(action);
            lastSearchProduct = key.getProduct();
            break;
        }
        case BUY: {
            key = this.getMatrixKeyForBuy(action);
            break;
        }
		default:
			break;
        }

        EnumMap<DynamicProduct, Product> productRow = this.getProductForRow(action, key);

        if (lastSearchProduct != null) {
            action.addDebug("Dynamic. Last search product: " + lastSearchProduct);
            productRow.put(DynamicProduct.LAST, lastSearchProduct);
        }
        return this.buildByPriority(ruleContent, productRow, sectionsToFill, action);
    }
    
    private MatrixKey getMatrixKeyForBuy(ActionRecommendation action) {

        BuyActivity buyActivity = action.getBuyActivity();
        Product pr = buyActivity.getProduct();
        LastActionMatrix lastAction = LastActionMatrix.getRange(action.getLastActionDays(pr));
        AnticipationMatrix anticipation = AnticipationMatrix.getRange(action.getAnticipationDays(pr));
        RouteType rt = pr == Product.FLIGHTS ? (new FlightData(buyActivity.getActivity().getAction())).routeType() : null;
        String destination = buyActivity.getActivity().getDestination();
        return new MatrixKey(SearchCount.NA, true, pr, lastAction, anticipation, action.getCountryCode(), rt, destination);
    }

    private MatrixKey getMatrixKeyForSearch(ActionRecommendation action) {

        UserActivity lastActivity = action.getSearchActivity().getLastActivity();
        Product pr = lastActivity.getProduct();
        Product lastSearchProduct = pr;
        String destination = lastActivity.getDestination();
        // TODO: borrar estos overrides cuando es implemente combined products...
        if (lastSearchProduct == Product.COMBINED_PRODUCTS) {
            lastSearchProduct = Product.FLIGHTS;
        }

        if (ProductCountrySupportUtils.isMissing(action.getCountryCode(), lastSearchProduct)) {
            lastSearchProduct = Product.HOTELS;
        }

        LastActionMatrix lastAction = LastActionMatrix.getRange(lastActivity.getLastActionDays());
        AnticipationMatrix anticipation = AnticipationMatrix.getRange(lastActivity.getAnticipationDays());
        RouteType rt = lastSearchProduct == Product.FLIGHTS ? (new FlightData(lastActivity.getAction())).routeType() : null;

        return new MatrixKey(lastActivity.getSearchCount(), false, lastSearchProduct, lastAction, anticipation,
            action.getCountryCode(), rt, destination);
    }

    private MatrixKey getMatrixKeyForNoHistory(ActionRecommendation action) {
        return new MatrixKey(null, null, null, null, null, action.getCountryCode(), null, null);
    }

    private RuleContentPriority buildByPriority(SectionRuleContent ruleOriginal,
        EnumMap<DynamicProduct, Product> productRow, EnumSet<SectionsEnum> sectionsToFill, ActionRecommendation action) {

        RuleContentPriority rcp = new RuleContentPriority(ruleOriginal);

        for (SectionsEnum section : SectionsEnum.ORIGINAL_SECTIONS) {

            switch (ruleOriginal.get(section).getType()) {

            case DYNAMIC_PRODUCT: {
                DynamicProduct dynProduct = ((DynamicRankingRuleSection) ruleOriginal.get(section)).getDynamicProduct();
                Product pr = productRow.get(dynProduct);

                Preconditions.checkNotNull(pr, "No product for" + dynProduct);
                SectionRuleBuilder.copy(ruleOriginal).fillDynamicRule(section, pr).build();

                RuleSection ruleSectionOriginal = ruleOriginal.get(section);

                RuleSectionPriority sectionPriority = new RuleSectionPriority(pr, ruleSectionOriginal.getFunction(),
                    ruleSectionOriginal.getUsage());

                rcp.add(section, sectionPriority);
                break;
            }

            case DYNAMIC_FUNCTION: {
                Product pr = ((DynamicFunctionRuleSection) ruleOriginal.get(section)).getProduct();
                Preconditions.checkNotNull(pr);

                SectionRuleBuilder.copy(ruleOriginal).fillDynamicRule(section, pr).build();

                RuleSection ruleSectionOriginal = ruleOriginal.get(section);

                List<SectionFunctionContent> functions = this.getAllFunctions(action, pr, section);

                RuleSectionPriority sectionPriority = new RuleSectionPriority(pr, functions, ruleSectionOriginal.getUsage());

                rcp.add(section, sectionPriority);
                break;
            }


            case DYNAMIC_PRODUCT_FUNCTION: {

                DynamicProduct dynProduct = ((DynamicProductFunctionRuleSection) ruleOriginal.get(section))
                    .getDynamicProduct();
                Product pr = productRow.get(dynProduct);

                Preconditions.checkNotNull(pr, "No product for" + dynProduct);

                List<SectionFunctionContent> functions = this.getAllFunctions(action, pr, section);

                RuleSection ruleSectionOriginal = ruleOriginal.get(section);

                RuleSectionPriority sectionPriority = new RuleSectionPriority(pr, functions, ruleSectionOriginal.getUsage());

                rcp.add(section, sectionPriority);

                break;
            }

            case STATIC: {

                RuleSection ruleSectionOrig = ruleOriginal.get(section);

                Preconditions.checkNotNull(ruleSectionOrig.getProduct(), "No product for" + ruleSectionOrig);

                RuleSectionPriority sectionPriority = new RuleSectionPriority(ruleSectionOrig.getProduct(),
                    ruleSectionOrig.getFunction(), ruleSectionOrig.getUsage());

                rcp.add(section, sectionPriority);

                break;
            }

            default:
                throw new IllegalArgumentException("Cant handle " + ruleOriginal.get(section).getType());
            }

        }

        return rcp;
    }

    /**
     * Based on the {@link ActionRecommendation} and the {@link Product} to offer we get the {@link Function}s to call.
     */
    private List<SectionFunctionContent> getAllFunctions(ActionRecommendation action, Product offer, SectionsEnum section) {

        List<SectionFunctionContent> functions = Lists.newArrayList();

        SectionType sectionType = SectionType.getSectionType(section);

        VisitFlow visitFlow = action.getVisitFlow(offer);
        VisitCity visitCity = VisitCity.getVisitCity(action.getLastDestination());

        QueryFunctionBuilder queryBuilder = QueryFunctionBuilder.create()//
            .offer(offer)//
            .activity(action.getActivityType())//
            .section(sectionType)//
            .bought(action.getBuyActivity() == null ? null : action.getBuyActivity().getProduct())//
            .visitFlow(visitFlow)//
            .visitCity(visitCity);//


        for (FunctionPriority priority : FunctionPriority.values()) {

            QueryFunction query = QueryFunctionBuilder.create(queryBuilder).priority(priority).build();

            if (this.functionEngine.hasRuleDefined(query)) {
                SectionFunctionContent function = this.functionEngine.query(query);
                functions.add(function);
                action.addDebug("   " + section + ": " + query + " -> " + function);
            }
        }

        Preconditions.checkArgument(!functions.isEmpty(), "No functions found for %s", queryBuilder);

        return functions;
    }

    private EnumMap<DynamicProduct, Product> getProductForRow(ActionRecommendation action, MatrixKey key) {
        List<Product> productByPriority = this.getDynamicProductByPriority(action, key);

        action.addDebug("Dynamic product list to use: " + productByPriority);

        Iterator<Product> productIterator = productByPriority.iterator();

        EnumMap<DynamicProduct, Product> productRow = new EnumMap<DynamicProduct, Product>(DynamicProduct.class);

        for (DynamicProduct dynamic : DynamicProduct.rows()) {

            Product pr = productIterator.next();

            // check that the product is supported for the country
            while (ProductCountrySupportUtils.isMissing(action.getCountryCode(), pr) && productIterator.hasNext()) {
                pr = productIterator.next();
            }

            productRow.put(dynamic, pr);

            // Product to offer for ROW1, ROW2 and ROW3 can't be the same
            if (dynamic == DynamicProduct.THIRD) {
                while (productRow.get(DynamicProduct.FIRST) == productRow.get(DynamicProduct.SECOND)
                    && productRow.get(DynamicProduct.SECOND) == pr && productIterator.hasNext()) {
                    action.addDebug("Dynamic. ROW1, ROW2 and ROW3 with product " + pr + " so using next product");
                    pr = productIterator.next();
                }
                productRow.put(dynamic, pr);
            }
        }

        action.addDebug("Product by row: " + productRow);

        return productRow;
    }

    /**
     * <p>If the key is not found the country key will be used </p>
     * <p>If only one product is taken from the key another product from the country will be taken</p>
     */
    private List<Product> getDynamicProductByPriority(ActionRecommendation action, MatrixKey key) {

        ProductMetricCount pmc = this.buyMatrixService.getProductMetricCount(key);

        if (pmc == null) {
            return Lists.newArrayList(Product.HOTELS, Product.HOTELS, Product.HOTELS, Product.CARS);
        }

        List<Product> productsToOffer = this.sortByPriority(pmc, key);

        if (productsToOffer.size() < 3) {
            action.addDebug("Not enough products from key. Getting from country");
            key = MatrixKey.forCountry(action.getCountryCode());
            action.addDebug("Dynamic. Using: " + key);
            pmc = this.buyMatrixService.getMatrixCounter(key);
            List<Product> fromCountry = this.sortByPriority(pmc, key);
            productsToOffer.addAll(fromCountry);
        }

        // FIXME: removiendo COMBINED PRODUCT hasta que la gente de front lo soporte :-(
        Collections.replaceAll(productsToOffer, Product.COMBINED_PRODUCTS, Product.FLIGHTS);

        return productsToOffer;
    }

    /**
     * <p>If the buy probability for a product is greater than 50% it will be offered in 2 rows</p>
     */
    private List<Product> sortByPriority(ProductMetricCount pmc, MatrixKey key) {
        List<Pair<Product, Long>> pairs = pmc.getProductSortByHighProb();

        List<Product> productsToOffer = Lists.newArrayList();

        for (Pair<Product, Long> pair : pairs) {
            productsToOffer.add(pair.getLeft());
            if (pair.getRight() > 50) {
                productsToOffer.add(pair.getLeft());
            }
        }

        Preconditions.checkNotNull(productsToOffer, "Dynamic product list to offer is null");
        Preconditions.checkArgument(!productsToOffer.isEmpty(), "Dynamic product to offer is empty: Key: %s", key);
        return productsToOffer;
    }
}