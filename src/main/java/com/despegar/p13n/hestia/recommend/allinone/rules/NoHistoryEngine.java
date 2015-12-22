package com.despegar.p13n.hestia.recommend.allinone.rules;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.HomeProduct;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Function;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent.DecisionTreeSection;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent.QueryRuleSection;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent.RuleDefSection;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent.SectionRuleContent;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent.QueryRuleSection.QueryRuleSectionBuilder;
import com.despegar.p13n.hestia.recommend.allinone.title.SectionType;
import com.despegar.p13n.hestia.recommend.allinone.title.TitleData;
import com.despegar.p13n.hestia.recommend.allinone.title.TitleEngine;
import com.despegar.p13n.hestia.utils.ItemUtils;
import com.despegar.p13n.hestia.utils.ProductCountrySupportUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class NoHistoryEngine {

    protected static final Logger LOG = LoggerFactory.getLogger(NoHistoryEngine.class);
    private TitleEngine titleEngine;
    private SectionFunctionEngine functionEngine;
    private DecisionTreeSection noHistoryTree = new DecisionTreeSection();
    private volatile boolean started = false;



    public NoHistoryEngine(TitleEngine titleEngine, SectionFunctionEngine functionEngine) {
        this.titleEngine = titleEngine;
        this.functionEngine = functionEngine;
    }

    public void start() {
        this.started = true;
    }

    public void addForNoHistory(RuleDefSection rta, SectionRuleContent ruleHomeContent) {
        Preconditions.checkState(!this.started, "Can't add. Engine has started");
        this.noHistoryTree.addRule(rta, ruleHomeContent);
    }

    public SectionRuleContent getNoHistoryPriority(ActionRecommendation action, Product home) {
        QueryRuleSection noHistoryQuery = this.buildQueryForNoHistory(home);
        return this.getSectionRuleContent(action, noHistoryQuery);
    }

    /**
     * <p>Returns a RuleContentPriority, with products and functions priorities already set.</p>
     * 
     * <p>Based on the country support we get rules based on this priority:</p>
     * 
     * <ul>
     *  <li>Rules for a country if it is specified</li>
     *  <li>Rules for a "only hotels" country if it is specified</li>
     *  <li>Rules for a "some products" country. The products offered in this group may be not supported in all countries.
     *  If the requested country supports all products in the group we use this</li>
     *  <li>Rules for a "all products" country. All countries support all products in this group of rules</li>
     * </ul>
     * 
     */
    public SectionRuleContent getSectionRuleContent(ActionRecommendation action, QueryRuleSection query) {

        boolean finished = false;
        CountrySupport support = CountrySupport.fromCountry(action.getCountryCode());
        SectionRuleContent ruleContent = null;
        do {
            query.setPrSupport(support);
            if (this.noHistoryTree.hasValue(query)) {
                action.addDebug("Rule query content: " + query);
                ruleContent = this.noHistoryTree.getValue(query);
                Preconditions.checkNotNull(ruleContent);
                if (this.areRuleSupported(action.getCountryCode(), ruleContent)) {
                    finished = true;
                    break;
                }
            }
            support = CountrySupport.nextPriority(support);
        } while (!finished);

        action.addDebug("---");
        return ruleContent;
    }

    /**
     * Returns true if all the products offered by this rule content are supported by the given country.
     */
    private boolean areRuleSupported(CountryCode cc, SectionRuleContent originalRuleContent) {

        Set<Product> products = Sets.newHashSet(//
            originalRuleContent.get(SectionsEnum.OFFER).getProduct(),//
            originalRuleContent.get(SectionsEnum.ROW1).getProduct(), //
            originalRuleContent.get(SectionsEnum.ROW2).getProduct(), //
            originalRuleContent.get(SectionsEnum.ROW3).getProduct());//

        return ProductCountrySupportUtils.areAllSupported(cc, products);
    }

    public QueryRuleSection buildQueryForNoHistory(Product home) {
        return QueryRuleSectionBuilder.create()//
            .activity(ActivityType.NO_HISTORY)//
            .lastAction(0)//
            .anticipation(0)//
            .bought(null)//
            .home(home)//
            .build();
    }

    public String noHistoryFallbackDebug(SectionsEnum section, boolean end) {
        return "-----[No_HISTORY fallback for " + section + " - " + (end ? "END" : "START") + "...]------";
    }

    public HomeProduct buildHomeForProduct(ActionRecommendation action, Product home) {
        QueryRuleSection query = this.buildQueryForNoHistory(home);
        HomeProduct content = this.buildHome(action, query);

        if (this.isHomeFull(content)) {
            return content;
        } else {
            action.addDebug("Can't build home");
            return null;
        }
    }

    public boolean isHomeFull(HomeProduct home) {

        return home != null && home.getSpecialOffersModule() != null && //
            home.getSpecialOffersModule().get(0).getOffer() != null && //
            this.isRowModulesFull(home.getRowModules());

    }

    private boolean isRowModulesFull(List<RowHome> list) {
        return list.size() >= 3 && list.get(0) != null && list.get(1) != null && list.get(2) != null;
    }

    private HomeProduct buildHome(ActionRecommendation action, QueryRuleSection query) {

        action.addDebug("-----[Getting rules...]-----");

        Product home = query.getHome();
        action.addDebug(this.noHistoryFallbackDebug(SectionsEnum.OFFER, false));
        List<Offer> offers = this.buildNoHistoryOffers(action, home, this.getSectionRuleContent(action, query));
        action.addDebug(this.noHistoryFallbackDebug(SectionsEnum.OFFER, true));

        List<RowHome> rows = Lists.newArrayList();
        List<SectionsEnum> rowSections = Lists.newArrayList(SectionsEnum.ROW1, SectionsEnum.ROW2, SectionsEnum.ROW3);

        for (int i = 0; i < rowSections.size(); i++) {
            SectionsEnum row = rowSections.get(i);
            action.addDebug(this.noHistoryFallbackDebug(row, false));
            RowHome rowHome = this.buildNoHistoryRow(action, this.getNoHistoryPriority(action, home), home, row);
            rows.add(rowHome);
            action.addDebug(this.noHistoryFallbackDebug(row, true));
        }
        return new HomeProduct(offers, rows);
    }

    public List<Offer> buildNoHistoryOffers(ActionRecommendation action, Product home, SectionRuleContent sectionRuleContent) {

        action.setCurrentSection(SectionsEnum.OFFER);
        action.setTitleData(new TitleData());

        RuleSection offerSecction = sectionRuleContent.getOffer();
        Function offerFunction = offerSecction.getFunction();
        final SectionFunction sectionFunction = this.functionEngine.getSectionFunction(offerFunction);
        List<Offer> offers = sectionFunction.buildOffers(home, offerSecction.getProduct(), action, offerFunction.getParam());

        if (offers == null) {
            action.addDebug(SectionsEnum.OFFER + ": " + 0);
            offerSecction.getUsage().incEmpty();
            action.releaseUniqueIds(home, offerSecction.getProduct(), SectionsEnum.OFFER);
        } else {
            action.addDebug(SectionsEnum.OFFER + ": " + 1);
            action.addDebug(SectionsEnum.OFFER, offerSecction);
            offerSecction.getUsage().incFull();
            // override with the dynamic title only when the section is full
            this.titleEngine.updateTitle(action, offerSecction, offers);
            ItemUtils.checkItemSupport(offerFunction.getCode(), offers);
            action.markFunctionAsBeingUsed(SectionType.OFFER, home, offerSecction.getProduct(), offerFunction);
        }
        return offers;
    }

    public RowHome buildNoHistoryRow(ActionRecommendation action, SectionRuleContent sectionRuleContent, Product home,
        SectionsEnum section) {

        action.setCurrentSection(section);
        action.setTitleData(new TitleData());
        RowHome rowHome = null;


        Function function = sectionRuleContent.get(section).getFunction();
        final SectionFunction sectionFunction = this.functionEngine.getSectionFunction(function);
        RuleSection ruleSection = new RuleSection(sectionRuleContent.get(section).getProduct(), function);
        SectionType sectionType = SectionType.getSectionType(section);
        rowHome = sectionFunction.buildRow(home, ruleSection.getProduct(), action, function.getParam());

        if (rowHome == null) {
            action.addDebug(section + ": " + 0);
            sectionRuleContent.get(section).getUsage().incEmpty();
            action.releaseUniqueIds(home, ruleSection.getProduct(), section);
        } else {
            action.addDebug(section + ": " + rowHome.getOffers().size());
            action.addDebug(section, ruleSection);
            sectionRuleContent.get(section).getUsage().incFull();
            // override with the dynamic title only when the section is full
            this.titleEngine.updateTitle(action, ruleSection, rowHome);
            ItemUtils.checkItemSupport(function.getCode(), rowHome.getHighlighted());

            for (ItemHome itemHome : rowHome.getOffers()) {
            	ItemUtils.checkItemSupport(function.getCode(), itemHome);
            }
            action.markFunctionAsBeingUsed(sectionType, home, ruleSection.getProduct(), ruleSection.getFunction());
        }
        return rowHome;
    }

    public List<String> dumpRulesAsString(Map<String, String> filterMap, String separator) {
        return this.noHistoryTree.dumpLastResortAsString(filterMap, separator);
    }

}
