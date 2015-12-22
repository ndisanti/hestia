package com.despegar.p13n.hestia.recommend.allinone.rules;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.HomeContent;
import com.despegar.p13n.hestia.api.data.model.HomeProduct;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.api.data.model.RulesVersion;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Function;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.functionpriority.SectionFunctionContent;
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
import com.newrelic.api.agent.Trace;


public class SectionRuleEngine
    implements RuleEngine {

    protected static final Logger LOG = LoggerFactory.getLogger(SectionRuleEngine.class);
    private volatile boolean started = false;
    private NoHistoryEngine noHistoryEngine;
    private LastResortEngine lastResortEngine;
    private RuleContentFiller ruleContentFiller;
    private SectionFunctionEngine functionEngine;
    private TitleEngine titleEngine;
    private Map<RulesVersion, DecisionTreeSection> versionMap;
    private HomeContentValidator validator;

    public SectionRuleEngine(NoHistoryEngine noHistoryEngine, RuleContentFiller ruleContentFiller,
        SectionFunctionEngine functionEngine, TitleEngine titleEngine, HomeContentValidator validator,
        LastResortEngine lastResortEngine) {
        this.noHistoryEngine = noHistoryEngine;
        this.ruleContentFiller = ruleContentFiller;
        this.functionEngine = functionEngine;
        this.titleEngine = titleEngine;
        this.validator = validator;
        this.initStrategyMap();
        this.lastResortEngine = lastResortEngine;
    }

    private void initStrategyMap() {
        this.versionMap = new HashMap<RulesVersion, DecisionTreeSection>();
        this.versionMap.put(RulesVersion.DYNAMIC_SERVICE, new DecisionTreeSection());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.despegar.euler.recommend.allinone.rules.RuleEngine#start()
     */
    @Override
    public void start() {
        this.started = true;
        LOG.info("Section rule engine started.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.despegar.euler.recommend.allinone.rules.RuleEngine#checkRanges()
     */
    @Override
    public void checkRanges() {
        this.versionMap.get(RulesVersion.DYNAMIC_SERVICE).checkRanges();
    }


    /*
     * (non-Javadoc)
     * 
     * @see
     * com.despegar.euler.recommend.allinone.rules.RuleEngine#buildHomeContent(com.despegar.euler.recommend.allinone
     * .ActionRecommendation)
     */
    @Override
    @Trace
    public HomeContent buildHomeContent(ActionRecommendation action) {

        Preconditions.checkState(this.started, "Engine hasn't started");
        HomeContent content = new HomeContent();

        for (Product home : action.getHomes()) {
            action.addDebug("-------------------- Building content for: " + home + " --------------------");
            HomeProduct homeProduct = this.buildHomeForProduct(action, home);
            // if a single home cant be built we default
            if (homeProduct != null) {
                content.addProduct(home, homeProduct);
                this.validator.validate(home, content, action);
            }
        }
        content.setLastActivity(action.getActivityType().name());
        return content;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.despegar.euler.recommend.allinone.rules.RuleEngine#buildHomeForProduct(com.despegar.euler.recommend.allinone
     * .ActionRecommendation, com.despegar.euler.api.data.model.Product)
     */
    @Override
    public HomeProduct buildHomeForProduct(ActionRecommendation action, Product home) {

        switch (action.getActivityType()) {
        case NO_HISTORY:
            return this.noHistoryEngine.buildHomeForProduct(action, home);

        case LAST_RSRT:
            return this.lastResortEngine.buildLastResoltHome(home, action.getCountryCode(), action.getLanguage());
        default:
            QueryRuleSection query = this.buildQuery(action, home);
            HomeProduct content = this.buildHome(action, query);

            if (this.isHomeFull(content)) {
                return content;
            } else {
                action.addDebug("Can't build home");
                return null;
            }
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

    /**
     * <p>Builds the home content based on a given query.</p>
     * 
     * <p>If a section cant be filled it will use the "NO_HISTORY" rule as fallback for that section</p>
     */
    private HomeProduct buildHome(ActionRecommendation action, QueryRuleSection query) {

        action.addDebug("-----[Getting rules...]-----");

        RuleContentPriority fullRuleContentPriority = this.buildRuleContentPriority(action, query);
        Product home = query.getHome();
        List<Offer> offers = this.buildOffers(action, home, fullRuleContentPriority);

        if (offers == null) {
            offers = this.buildNoHistoryOffer(action, home);
        }

        List<RowHome> rows = Lists.newArrayList();
        List<SectionsEnum> rowSections = Lists.newArrayList(SectionsEnum.ROW1, SectionsEnum.ROW2, SectionsEnum.ROW3);

        for (int i = 0; i < rowSections.size(); i++) {

            SectionsEnum row = rowSections.get(i);
            RowHome rowHome = this.buildRow(action, fullRuleContentPriority, home, row);
            rows.add(rowHome);

            if (rowHome == null) {
                this.buildNoHistoryRow(action, home, rows, i, row);
            }
        }
        return new HomeProduct(offers, rows);
    }

    private void buildNoHistoryRow(ActionRecommendation action, Product home, List<RowHome> rows, int i, SectionsEnum row) {
        RowHome rowHome;
        action.addDebug(this.noHistoryEngine.noHistoryFallbackDebug(row, false));
        rowHome = this.noHistoryEngine.buildNoHistoryRow(action, this.noHistoryEngine.getNoHistoryPriority(action, home),
            home, row);
        rows.set(i, rowHome);
        action.addDebug(this.noHistoryEngine.noHistoryFallbackDebug(row, true));
    }

    private List<Offer> buildNoHistoryOffer(ActionRecommendation action, Product home) {
        List<Offer> offers;
        action.addDebug(this.noHistoryEngine.noHistoryFallbackDebug(SectionsEnum.OFFER, false));
        offers = this.noHistoryEngine.buildNoHistoryOffers(action, home,
            this.noHistoryEngine.getNoHistoryPriority(action, home));
        action.addDebug(this.noHistoryEngine.noHistoryFallbackDebug(SectionsEnum.OFFER, true));
        return offers;
    }

    public List<Offer> buildOffers(ActionRecommendation action, Product home, RuleContentPriority ruleContentPriority) {
        RuleSectionPriority ruleSectionPriorities = ruleContentPriority.get(SectionsEnum.OFFER);

        action.setCurrentSection(SectionsEnum.OFFER);
        action.setTitleData(new TitleData());

        List<Offer> offers = null;

        for (SectionFunctionContent functionContent : ruleSectionPriorities.getFunctions()) {

            Function function = functionContent.getFunction();

            final SectionFunction sectionFunction = this.functionEngine.getSectionFunction(function);

            RuleSection ruleSection = new RuleSection(ruleSectionPriorities.getProduct(), function);

            // Dynamic Service: if the function is being used we continue with the next function
            if (action.isFunctionBeingUsed(SectionType.OFFER, home, ruleSection.getProduct(), ruleSection.getFunction())) {
                continue;
            }

            offers = sectionFunction.buildOffers(home, ruleSectionPriorities.getProduct(), action, function.getParam());

            if (offers == null) {
                functionContent.getUsage().incEmpty();
                action.releaseUniqueIds(home, ruleSectionPriorities.getProduct(), SectionsEnum.OFFER);
            } else {
                action.addDebug(SectionsEnum.OFFER, ruleSection);
                functionContent.getUsage().incFull();
                // override with the dynamic title only when the section is full
                this.titleEngine.updateTitle(action, ruleSection, offers);
                ItemUtils.checkItemSupport(function.getCode(), offers);
                action.markFunctionAsBeingUsed(SectionType.OFFER, home, ruleSection.getProduct(), ruleSection.getFunction());
                break;
            }
        }


        if (offers == null) {
            action.addDebug(SectionsEnum.OFFER + ": " + 0);
            ruleContentPriority.getOriginalRuleContent().get(SectionsEnum.OFFER).getUsage().incEmpty();
        } else {
            action.addDebug(SectionsEnum.OFFER + ": " + 1);
            ruleContentPriority.getOriginalRuleContent().get(SectionsEnum.OFFER).getUsage().incFull();
        }

        return offers;
    }

    public RowHome buildRow(ActionRecommendation action, RuleContentPriority ruleSectionPriority, Product home,
        SectionsEnum section) {

        action.setCurrentSection(section);
        action.setTitleData(new TitleData());
        RowHome rowHome = null;

        for (SectionFunctionContent functionContent : ruleSectionPriority.get(section).getFunctions()) {

            Function function = functionContent.getFunction();
            final SectionFunction sectionFunction = this.functionEngine.getSectionFunction(function);
            RuleSection ruleSection = new RuleSection(ruleSectionPriority.get(section).getProduct(), function);
            SectionType sectionType = SectionType.getSectionType(section);

            // Dynamic Service: if the function is being used we continue with the next function
            if (action.isFunctionBeingUsed(sectionType, home, ruleSection.getProduct(), ruleSection.getFunction())) {
                continue;
            }

            rowHome = sectionFunction.buildRow(home, ruleSection.getProduct(), action, function.getParam());

            if (rowHome == null) {
                functionContent.getUsage().incEmpty();
                action.releaseUniqueIds(home, ruleSection.getProduct(), section);
            } else {
                action.addDebug(section, ruleSection);
                functionContent.getUsage().incFull();
                // override with the dynamic title only when the section is full
                this.titleEngine.updateTitle(action, ruleSection, rowHome);
                ItemUtils.checkItemSupport(function.getCode(), rowHome.getHighlighted());

                for (ItemHome itemHome : rowHome.getOffers()) {
                	ItemUtils.checkItemSupport(function.getCode(), itemHome);
                }
                action.markFunctionAsBeingUsed(sectionType, home, ruleSection.getProduct(), ruleSection.getFunction());
                break;
            }
        }
        if (rowHome == null) {
            action.addDebug(section + ": " + 0);
            ruleSectionPriority.getOriginalRuleContent().get(section).getUsage().incEmpty();
        } else {
            action.getSectionsDestinations().get(section).addAll(action.getTitleData().getDestinations());
            rowHome.setMonoDestination(action.getTitleData().isSingleDestination());
            action.addDebug(section + ": " + rowHome.getOffers().size());
            ruleSectionPriority.getOriginalRuleContent().get(section).getUsage().incFull();
        }
        return rowHome;
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
    public RuleContentPriority buildRuleContentPriority(ActionRecommendation action, QueryRuleSection query) {

        boolean finished = false;
        RuleContentPriority fullRuleContentPriority = null;
        CountrySupport support = CountrySupport.fromCountry(action.getCountryCode());

        do {
            query.setPrSupport(support);
            if (this.versionMap.get(action.getVersion()).hasValue(query)) {
                action.addDebug("Rule query content: " + query);

                SectionRuleContent originalRuleContent = this.versionMap.get(action.getVersion()).getValue(query);
                Preconditions.checkNotNull(originalRuleContent);
                fullRuleContentPriority = this.ruleContentFiller.buildRuleContent(originalRuleContent, action);

                if (this.areRuleSupported(action.getCountryCode(), fullRuleContentPriority)) {
                    finished = true;
                    break;
                }
            }
            support = CountrySupport.nextPriority(support);
        } while (!finished);

        if (action.getVersion() == RulesVersion.DYNAMIC_SERVICE) {
            action.addDebug(fullRuleContentPriority);
        }
        action.addDebug("---");
        return fullRuleContentPriority;
    }

    /**
     * Returns true if all the products offered by this rule content are supported by the given country.
     */
    private boolean areRuleSupported(CountryCode cc, RuleContentPriority ruleContent) {

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
     * @see com.despegar.euler.recommend.allinone.rules.RuleEngine#dumpRulesAsString(java.util.Map, java.lang.String,
     * com.despegar.euler.recommend.allinone.rules.RulesVersion)
     */
    @Override
    public List<String> dumpRulesAsString(Map<String, String> filterMap, String separator, RulesVersion rulesVersion) {
        return this.versionMap.get(rulesVersion).dumpAsString(filterMap, separator);
    }


    private QueryRuleSection buildQuery(ActionRecommendation action, Product home) {

        switch (action.getActivityType()) {

        case BUY:

            return QueryRuleSectionBuilder.create()//
                .activity(ActivityType.BUY)//
                .lastAction(action.getLastActionDays(home))//
                .anticipation(action.getAnticipationDays(home))//
                .bought(action.getBuyActivity().getProduct())//
                .home(home)//
                .build();

        case SEARCH:

            return QueryRuleSectionBuilder.create()//
                .activity(ActivityType.SEARCH)//
                .lastAction(action.getLastActionDays(home))//
                .anticipation(action.getAnticipationDays(home))//
                .bought(null)//
                .home(home)//
                .build();
        default:
            throw new IllegalArgumentException("Activity type: " + action.getActivityType());
        }
    }

    public void addForDynamicServ(RuleDefSection rule, SectionRuleContent rc) {
        Preconditions.checkState(!this.started, "Can't add. Engine has started");
        this.versionMap.get(RulesVersion.DYNAMIC_SERVICE).addRule(rule, rc);
    }

    @Override
    public EnumSet<RulesVersion> supportedVersions() {
        return EnumSet.of(RulesVersion.DYNAMIC_SERVICE);
    }

		 public void addForDynamicPr(RuleDefSection rule, SectionRuleContent rc) {
		        Preconditions.checkState(!this.started, "Can't add. Engine has started");
		        this.versionMap.get(RulesVersion.DYNAMIC_PRODUCT).addRule(rule, rc);
		    }
}
