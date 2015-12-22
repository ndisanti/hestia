package com.despegar.p13n.hestia.recommend.allinone.rules;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Language;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.HomeProduct;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.ItemType;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Function;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent.DecisionTreeSection;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent.QueryRuleSection;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent.RuleDefSection;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent.SectionRuleContent;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent.QueryRuleSection.QueryRuleSectionBuilder;
import com.despegar.p13n.hestia.recommend.allinone.title.SectionType;
import com.despegar.p13n.hestia.recommend.allinone.title.TitleData;
import com.despegar.p13n.hestia.recommend.allinone.title.TitleEngine;
import com.despegar.p13n.hestia.service.MultiObjectVersionService;
import com.despegar.p13n.hestia.utils.ProductCountrySupportUtils;
import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.newrelic.api.agent.NewRelic;


public class LastResortEngine {

    private MultiObjectVersionService multiObjectService;
    private DecisionTreeSection decisionTree = new DecisionTreeSection();
    private HomeContentValidator validator;
    private SectionFunctionEngine functionEngine;

    private TitleEngine titleEngine;
    private volatile Cache<LastResortKey, HomeProduct> lastResort;
    private volatile boolean started = false;
    protected static final Logger LOG = LoggerFactory.getLogger(LastResortEngine.class);

    public LastResortEngine(HomeContentValidator validator, TitleEngine titleEngine,
        MultiObjectVersionService multiObjectService, SectionFunctionEngine functionEngine) {
        this.validator = validator;
        this.titleEngine = titleEngine;
        this.multiObjectService = multiObjectService;
        this.functionEngine = functionEngine;
    }

    public void start() {
        this.buildLastResort();;
        this.started = true;
    }

    public void buildLastResort() {
        if (!this.validator.isLastResortEnabled()) {
            return;
        }

        try {

            Cache<LastResortKey, HomeProduct> cache = CacheBuilder.newBuilder().maximumSize(10000).recordStats().build();

            LOG.info("Filling last resort cache....");
            for (CountryCode cc : CountrySupport.RELEVANTS_CC) {
                for (Product homeProduct : ProductCountrySupportUtils.getHomesSupported(cc)) {
                    for (Language language : Language.values()) {

                        HomeProduct content = this.buildLastResoltHome(homeProduct, cc, language);

                        if (content != null) {

                            if (this.validator.isValidateLastResort()) {
                                this.validator.validate(homeProduct, cc, language, content, null);
                            }
                            cache.put(new LastResortKey(homeProduct, cc, language), content);
                        }
                    }
                }

            }
            LOG.info("End filling last resort cache. Size:" + cache.size());

            this.lastResort = cache;

        } catch (Exception e) {
            LOG.error("Error filling last resort cache.", e);
            NewRelic.noticeError("Error filling last resort cache.");
            throw new RuntimeException(e);
        }
    }

    public HomeProduct buildLastResoltHome(Product home, CountryCode cc, Language language) {

        QueryRuleSection lastResortQuery = this.getLastResortQuery(home);
        // calculate last resort home for significant countries


        ActionRecommendation emptyAction = new ActionRecommendation(null, home, cc, language, null, null, null, null);
        emptyAction.setCurrentHome(home);
        // TODO: revisar
        emptyAction.setLastResort(true);

        SectionRuleContent sectionRuleContent = this.getSectionRuleContent(emptyAction, lastResortQuery);
        List<Offer> offers = this.buildOffers(emptyAction, lastResortQuery.getHome(), sectionRuleContent);
        List<RowHome> rows = Lists.newArrayList();
        List<SectionsEnum> rowSections = Lists.newArrayList(SectionsEnum.ROW1, SectionsEnum.ROW2, SectionsEnum.ROW3);

        for (int i = 0; i < rowSections.size(); i++) {
            SectionsEnum row = rowSections.get(i);
            final RowHome rowHome = this.buildRow(emptyAction, sectionRuleContent, lastResortQuery.getHome(), row);
            rows.add(rowHome);
        }
        this.multiObjectService.addObjectsInLastResort(home, offers, rows, language, cc);
        return new HomeProduct(offers, rows);
    }

    public QueryRuleSection getLastResortQuery(Product home) {
        return QueryRuleSectionBuilder.create()//
            .activity(ActivityType.LAST_RSRT)//
            .lastAction(0)//
            .anticipation(0)//
            .bought(null)//
            .home(home)//
            .build();
    }


    public List<Offer> buildOffers(ActionRecommendation action, Product home, SectionRuleContent sectionRuleContent) {

        action.setCurrentSection(SectionsEnum.OFFER);
        action.setTitleData(new TitleData());

        RuleSection offerSection = sectionRuleContent.getOffer();
        Function offerfunction = offerSection.getFunction();
        final SectionFunction sectionFunction = this.functionEngine.getSectionFunction(offerfunction);
        List<Offer> offers = sectionFunction.buildOffers(home, offerSection.getProduct(), action, offerfunction.getParam());

        if (offers == null) {
            action.addDebug(SectionsEnum.OFFER + ": " + 0);
            offerSection.getUsage().incEmpty();
            action.releaseUniqueIds(home, offerSection.getProduct(), SectionsEnum.OFFER);
        } else {
            action.addDebug(SectionsEnum.OFFER + ": " + 1);
            action.addDebug(SectionsEnum.OFFER, offerSection);
            offerSection.getUsage().incFull();
            this.titleEngine.updateTitle(action, offerSection, offers);
            this.checkItemSupport(offerfunction.getCode(), offers);
            action.markFunctionAsBeingUsed(SectionType.OFFER, home, offerSection.getProduct(), offerSection.getFunction());
        }
        return offers;
    }

    public RowHome buildRow(ActionRecommendation action, SectionRuleContent sectionRuleContent, Product home,
        SectionsEnum section) {

        action.setCurrentSection(section);
        action.setTitleData(new TitleData());
        RowHome rowHome = null;

        final SectionFunction sectionFunction = this.functionEngine.getSectionFunction(sectionRuleContent.get(section)
            .getFunction());
        rowHome = sectionFunction.buildRow(home, sectionRuleContent.get(section).getProduct(), action,
            Function.LAST_RESORT.getParam());
        RuleSection ruleSection = new RuleSection(sectionRuleContent.get(section).getProduct(), Function.LAST_RESORT);

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
            this.checkItemSupport(Function.LAST_RESORT.getCode(), rowHome.getHighlighted());

            for (ItemHome itemHome : rowHome.getOffers()) {
                this.checkItemSupport(Function.LAST_RESORT.getCode(), itemHome);
            }
            action.markFunctionAsBeingUsed(SectionType.ROW, home, ruleSection.getProduct(), Function.LAST_RESORT);
        }
        return rowHome;
    }

    private void checkItemSupport(SectionFunctionCode functionCode, ItemHome itemHome) {

        if (itemHome == null) {
            return;
        }
        ItemType itemType = ItemType.getItemType(itemHome.getClass());
        if (!functionCode.getItemTypes().contains(itemType)) {

            String key = functionCode + "|" + itemType;
            NewRelic.noticeError("Not supported " + key);
        }
    }

    private void checkItemSupport(SectionFunctionCode functionCode, List<Offer> offers) {
        if (offers != null) {
            for (Offer offer : offers) {
                this.checkItemSupport(functionCode, offer.getOffer());
            }
        }
    }

    public SectionRuleContent getSectionRuleContent(ActionRecommendation action, QueryRuleSection lastResortQuery) {

        boolean finished = false;
        CountrySupport support = CountrySupport.fromCountry(action.getCountryCode());
        SectionRuleContent originalRuleContent = null;
        do {
            lastResortQuery.setPrSupport(support);
            if (this.decisionTree.hasValue(lastResortQuery)) {
                action.addDebug("Rule query content: " + this.decisionTree);
                originalRuleContent = this.decisionTree.getValue(lastResortQuery);
                Preconditions.checkNotNull(originalRuleContent);
                if (this.areRuleSupported(action.getCountryCode(), originalRuleContent)) {
                    finished = true;
                    break;
                }
            }
            support = CountrySupport.nextPriority(support);
        } while (!finished);

        action.addDebug("---");
        return originalRuleContent;
    }

    /**
     * Returns true if all the products offered by this rule content are supported by the given country.
     */
    private boolean areRuleSupported(CountryCode cc, SectionRuleContent ruleContent) {

        Set<Product> products = Sets.newHashSet(//
            ruleContent.get(SectionsEnum.OFFER).getProduct(),//
            ruleContent.get(SectionsEnum.ROW1).getProduct(), //
            ruleContent.get(SectionsEnum.ROW2).getProduct(), //
            ruleContent.get(SectionsEnum.ROW3).getProduct());//

        return ProductCountrySupportUtils.areAllSupported(cc, products);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.despegar.euler.recommend.allinone.rules.RuleEngine#getLastResortDefault(com.despegar.euler.api.data.model
     * .Product, com.despegar.euler.api.data.model.CountryCode, com.despegar.euler.api.data.model.Language, boolean)
     */
    public HomeProduct getLastResortDefault(Product home, CountryCode cc, Language language) {
        language = language == null ? Language.ES : language;

        return this.lastResort.getIfPresent(new LastResortKey(home, cc, language));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.despegar.euler.recommend.allinone.rules.RuleEngine#dumpLastResortRulesAsString(java.util.Map,
     * java.lang.String, com.despegar.euler.recommend.allinone.rules.RulesVersion)
     */
    public List<String> dumpLastResortRulesAsString(Map<String, String> filterMap, String separator) {
        return this.decisionTree.dumpLastResortAsString(filterMap, separator);
    }

    public void addForAny(RuleDefSection rta, SectionRuleContent ruleHomeContent) {
        Preconditions.checkState(!this.started, "Can't add. Engine has started");
        this.decisionTree.addRule(rta, ruleHomeContent);
    }

}
