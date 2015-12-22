package com.despegar.p13n.hestia.recommend.allinone.rules;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.despegar.p13n.euler.commons.client.model.HomeUtils;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.HomeContent;
import com.despegar.p13n.hestia.api.data.model.HomeProduct;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.api.data.model.RulesVersion;
import com.despegar.p13n.hestia.api.data.model.home.ItemTypeId;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.item.ItemHomeService;
import com.despegar.p13n.hestia.recommend.allinone.rules.builder.HomeRuleBuilder;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.ItemIdFuncCode;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.ItemIdFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.ProductFuncCode;
import com.despegar.p13n.hestia.recommend.allinone.rules.function.core.ProductFunction;
import com.despegar.p13n.hestia.recommend.allinone.rules.item.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.itemcontent.DecisionTreeHomeItem;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.itemcontent.HomeRuleContent;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.itemcontent.QueryRuleItem;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.itemcontent.RuleDefItem;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.itemcontent.QueryRuleItem.QueryRuleItemBuilder;
import com.despegar.p13n.hestia.recommend.allinone.title.TitleData;
import com.despegar.p13n.hestia.recommend.allinone.title.TitleEngine;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.newrelic.api.agent.Trace;

public class ItemRuleEngine
    implements RuleEngine {

    protected static final Logger LOG = LoggerFactory.getLogger(ItemRuleEngine.class);

    private volatile boolean started = false;

    private NoHistoryEngine noHistoryEngine;

    private LastResortEngine lastResortEngine;

    private final Map<ItemIdFuncCode, ItemIdFunction> itemFunctions;
    private final Map<ProductFuncCode, ProductFunction> prFunctions;

    private ItemHomeService itemHomeService;

    private TitleEngine titleEngine;

    private Map<RulesVersion, DecisionTreeHomeItem> versionMap;

    public ItemRuleEngine(Map<ItemIdFuncCode, ItemIdFunction> itemFunctions,
        Map<ProductFuncCode, ProductFunction> prFunctions, ItemHomeService itemHomeService, NoHistoryEngine noHistoryEngine,
        TitleEngine titleEngine, LastResortEngine lastResortEngine) {
        this.itemFunctions = itemFunctions;
        this.prFunctions = prFunctions;
        this.itemHomeService = itemHomeService;
        this.noHistoryEngine = noHistoryEngine;
        this.titleEngine = titleEngine;
        this.initStrategyMap();
        this.lastResortEngine = lastResortEngine;
    }

    private void initStrategyMap() {
        this.versionMap = new HashMap<RulesVersion, DecisionTreeHomeItem>();
        this.versionMap.put(RulesVersion.MONO_DESTINATION, new DecisionTreeHomeItem());
        this.versionMap.put(RulesVersion.MULTI_DESTINATION, new DecisionTreeHomeItem());

    }

    public void start() {
        this.started = true;
        LOG.info("Item rule engine started.");
    }

    public void checkRanges() {
        this.versionMap.get(RulesVersion.MONO_DESTINATION).checkRanges();
        this.versionMap.get(RulesVersion.MULTI_DESTINATION).checkRanges();
    }

    /**
     * <p>Builds home content with a fallback mechanism:</p>o
     * 
     * <p>Gets the rule based in the action and builds the home content based on that rule.</p>
     * 
     * <p>If some section for this home content is empty (null) we try to fill that empty section using the "no history" rule.</p>
     * 
     * <p>If some section remains empty, the home content is discarded and we return the last resort cached home content.</p>
     * 


     * <p>We discard the whole home content because we can not guarante that items are unique.</p>
     */
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
            }
        }
        content.setLastActivity(action.getActivityType().name());
        return content;
    }

    public HomeProduct buildHomeForProduct(ActionRecommendation action, Product home) {

        switch (action.getActivityType()) {
        case NO_HISTORY:
            return this.noHistoryEngine.buildHomeForProduct(action, home);
        case LAST_RSRT:
            return this.lastResortEngine.getLastResortDefault(home, action.getCountryCode(), action.getLanguage());
        default:
            QueryRuleItem query = this.buildQuery(action, home);
            return this.buildHome(action, query);
        }
    }

    /**
     * <p>Builds the home content based on a given query.</p>
     * 
     * <p>If a section cant be filled it will use the "NO_HISTORY" rule as fallback for that section</p>
     */
    private HomeProduct buildHome(ActionRecommendation action, QueryRuleItem query) {

        action.addDebug("-----[Getting rules...]-----");

        HomeRuleContent homeRuleContent = this.getHomeRuleContent(action, query);
        Product home = query.getHome();

        action.addDebug(homeRuleContent);

        action.addDebug("---");

        List<Offer> offers = this.buildOffers(action, home, homeRuleContent);

        List<RowHome> rows = Lists.newArrayList();
        List<SectionsEnum> rowSections = Lists.newArrayList(SectionsEnum.ROW1, SectionsEnum.ROW2, SectionsEnum.ROW3);

        action.addDebug("---");

        for (int i = 0; i < rowSections.size(); i++) {

            SectionsEnum row = rowSections.get(i);
            RowHome rowHome = this.buildRow(action, homeRuleContent, home, row);
            rows.add(rowHome);
            action.addDebug("---");
        }

        if (offers.isEmpty() || rows.get(0) == null || rows.get(2) == null || rows.get(1) == null) {
            return null;
        }
        return new HomeProduct(offers, rows);
    }

    /**
     * <p>Returns a HomeRuleContent</p>
     * 
     * <p>Based on the product country support we get rules</p>
     * 
     */
    private HomeRuleContent getHomeRuleContent(ActionRecommendation action, QueryRuleItem query) {

        boolean finished = false;
        HomeRuleContent homeRuleContent = null;
        CountrySupport support = CountrySupport.fromCountry(action.getCountryCode());

        do {
            query.setPrSupport(support);
            if (this.versionMap.get(action.getVersion()).hasValue(query)) {
                action.addDebug("Rule query content: " + query);
                homeRuleContent = this.versionMap.get(action.getVersion()).getValue(query);
                Preconditions.checkNotNull(homeRuleContent);
                finished = true;
                break;
            }
            support = CountrySupport.nextPriority(support);

        } while (!finished);
        action.addDebug("---");
        return homeRuleContent;
    }

    private List<Offer> buildOffers(ActionRecommendation action, Product home, HomeRuleContent homeRuleContent) {

        List<Offer> offers = Lists.newArrayList();
        List<ItemHome> items = this.buildItemList(action, home, homeRuleContent.getOffers(), SectionsEnum.OFFER);

        for (ItemHome item : items) {
            offers.add(new Offer(item, null));
        }
        this.titleEngine.updateTitle(action, offers);
        return offers;
    }

    private RowHome buildRow(ActionRecommendation action, HomeRuleContent homeRuleContent, Product home, SectionsEnum section) {

        List<RuleItem> ruleItemList = homeRuleContent.getRowList().get(section.ordinal() - 1);
        List<ItemHome> list = this.buildItemList(action, home, ruleItemList, section);

        if (list.isEmpty()) {
            return null;
        }
        RowHome row = new RowHome(null, list);
        row.setMonoDestination(action.getTitleData().isSingleDestination());
        action.getSectionsDestinations().get(section).addAll(action.getTitleData().getDestinations());
        this.titleEngine.updateTitle(action, row);
        return row;
    }

    private List<ItemHome> buildItemList(ActionRecommendation action, Product home, List<RuleItem> ruleItemList,
        SectionsEnum section) {
        action.setCurrentSection(section);
        action.setTitleData(new TitleData());

        List<ItemHome> items = Lists.newArrayList();

        for (RuleItem ruleItem : ruleItemList) {
            ItemHome item = this.buildItemHome(section, ruleItem, action);
            if (item != null) {
                items.add(item);
            }
        }

        boolean filled = section == SectionsEnum.OFFER ? items.size() >= HomeRuleBuilder.OFFERS_SIZE
            : items.size() >= HomeUtils.MIN_ROW_SIZE;

        if (items.isEmpty() || !filled) {
            action.addDebug(section + ": " + items.size());
            action.releaseUniqueIds(home, section);
            return Collections.emptyList();
        } else {
            action.addDebug(section, ruleItemList);
        }
        return items;
    }

    /**
     * Builds a unique home item
     */
    private ItemHome buildItemHome(SectionsEnum section, RuleItem ruleItem, ActionRecommendation action) {
        ItemIdFuncCode functionCode = ruleItem.getItemIdFunction().getFunctionCode();
        ProductFuncCode prFunctionCode = ruleItem.getProductFunction();
        ItemIdFunction itemIdFunction = this.itemFunctions.get(functionCode);

        Param param = ruleItem.getItemIdFunction().getParam();

        String itemId;

        Product pr = null;
        // no index, multidestino
        if (param.isEmpty()) {
            itemId = action.getFunctionPrecalc().getNextId(itemIdFunction, action);
            if (itemId == null) {
                return null;
            }
            pr = action.getFunctionPrecalc().getNextProduct(section, this.prFunctions.get(prFunctionCode), null, null,
                action);

        } else { // destino por indice
            itemId = action.getFunctionPrecalc().getId(itemIdFunction, action, param.getItemFuncIdx() - 1);
            if (itemId == null) {
                return null;
            }
            pr = action.getFunctionPrecalc().getNextProduct(section, this.prFunctions.get(prFunctionCode),
                itemIdFunction.getItemTypeId(), itemId, action);
        }

        ItemHome itemHome = this.itemHomeService.buildItemHome(itemIdFunction.getItemTypeId(), itemId, pr, action);

        if (itemHome != null) {

            if (itemIdFunction.getItemTypeId() == ItemTypeId.DESTINATION) {
                action.getTitleData().addDestination(itemId);
            }
            return itemHome;
        }
        return null;
    }

    private QueryRuleItem buildQuery(ActionRecommendation action, Product home) {

        switch (action.getActivityType()) {

        case BUY:

            return QueryRuleItemBuilder.create()//
                .activity(ActivityType.BUY)//
                .support(CountrySupport.SOME)//
                .home(home)//
                .build();

        case SEARCH:

            return QueryRuleItemBuilder.create()//
                .activity(ActivityType.SEARCH)//
                .support(CountrySupport.SOME)//
                .home(home)//
                .build();

        default:
            throw new IllegalArgumentException("Activity type: " + action.getActivityType());
        }
    }

    @Override
    public List<String> dumpRulesAsString(Map<String, String> filterMap, String separator, RulesVersion rulesVersion) {
        return this.versionMap.get(rulesVersion).dumpAsString(filterMap, separator);
    }

    @Override
    public EnumSet<RulesVersion> supportedVersions() {
        return EnumSet.of(RulesVersion.MONO_DESTINATION, RulesVersion.MULTI_DESTINATION);
    }

    public void addForMultiDestination(RuleDefItem rule, HomeRuleContent hrc) {
        Preconditions.checkState(!this.started, "Can't add. Engine has started");
        this.versionMap.get(RulesVersion.MULTI_DESTINATION).addRule(rule, hrc);
    }

    public void addForMonoDestination(RuleDefItem rule, HomeRuleContent hrc) {
        Preconditions.checkState(!this.started, "Can't add. Engine has started");
        this.versionMap.get(RulesVersion.MONO_DESTINATION).addRule(rule, hrc);
    }
}
