package com.despegar.p13n.hestia.recommend.allinone.title;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.despegar.p13n.euler.commons.client.model.Language;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.ItemHome;
import com.despegar.p13n.hestia.api.data.model.ItemType;
import com.despegar.p13n.hestia.api.data.model.Offer;
import com.despegar.p13n.hestia.api.data.model.RowHome;
import com.despegar.p13n.hestia.api.data.model.Title;
import com.despegar.p13n.hestia.api.data.model.home.ItemTypeId;
import com.despegar.p13n.hestia.external.geo.GeoService;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.i18n.I18nService;
import com.despegar.p13n.hestia.recommend.allinone.rules.HomeSupport;
import com.despegar.p13n.hestia.recommend.allinone.rules.RuleSection;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;
import com.despegar.p13n.hestia.utils.DumpUtils;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * Based on the user activity, the {@link ItemType} offer and the {@link SectionFunction}
 * used to build the section, it returns a {@link TitleListEnum}.
 */
@Service
public class TitleEngine {

    private Map<MonoProductTitleKey, ListCounter> monoProductsTitlesRules = Maps.newLinkedHashMap();
    private Map<MultiProductTitleKey, ListCounter> multiProductsTitlesRules = Maps.newLinkedHashMap();

    protected static final Logger LOGGER = LoggerFactory.getLogger(TitleEngine.class);

    private Set<MonoProductTitleKey> monoProductMissingKeys = Collections
        .newSetFromMap(new ConcurrentHashMap<MonoProductTitleKey, Boolean>());

    @Autowired
    private I18nService i18nService;

    @Autowired
    private GeoService geoService;


    @PostConstruct
    public void init() {
        new TitleRuleBuilder(this);
    }

    public void add(MonoProductTitleKey titleKey, TitleListEnum listTitle) {
        Preconditions.checkArgument(!this.monoProductsTitlesRules.containsKey(titleKey), "Title key already added %s",
            titleKey);
        this.monoProductsTitlesRules.put(titleKey, new ListCounter(listTitle));
    }

    public void add(MultiProductTitleKey titleKey, TitleListEnum listTitle) {
        Preconditions.checkArgument(!this.multiProductsTitlesRules.containsKey(titleKey), "Title key already added %s",
            titleKey);
        this.multiProductsTitlesRules.put(titleKey, new ListCounter(listTitle));
    }

    public List<String> dumpMultiProduct(Map<String, String> filterMap) {
        final String format = "%-15s %-10s %-30s %-30s  %-20s %-10s";

        List<String> rules = Lists.newArrayList();
        String header = String.format(format, "Activity", "Section", "singleDestination", "support", "List", "Call");
        rules.add(header);

        for (Entry<MultiProductTitleKey, ListCounter> entry : this.multiProductsTitlesRules.entrySet()) {

            ActivityType activity = entry.getKey().getActivityType();
            SectionType section = entry.getKey().getSectionType();
            boolean singleDestination = entry.getKey().isSingleDestination();
            HomeSupport support = entry.getKey().getSupport();
            ListCounter listCounter = entry.getValue();

            Map<String, String> keyValues = Maps.newHashMap();
            keyValues.put("activity", activity.toString());
            keyValues.put("section", section.toString());
            keyValues.put("singleDestination", String.valueOf(singleDestination));
            keyValues.put("support", support.toString());
            keyValues.put("list", listCounter.list.toString());

            if (!DumpUtils.shouldAddToDump(filterMap, keyValues)) {
                continue;
            }
            String row = String.format(format, activity, section, singleDestination, support, listCounter.list,
                listCounter.call.get());
            rules.add(row);
        }

        return rules;
    }

    public List<String> dumpMonoProduct(Map<String, String> filterMap) {

        final String format = "%-15s %-10s %-30s %-30s %-55s %-20s %-10s";

        List<String> rules = Lists.newArrayList();
        String header = String.format(format, "Activity", "Section", "ItemType", "Function", "Param", "List", "Call");
        rules.add(header);

        for (Entry<MonoProductTitleKey, ListCounter> entry : this.monoProductsTitlesRules.entrySet()) {

            ActivityType activity = entry.getKey().getActivityType();
            SectionType section = entry.getKey().getSectionType();
            ItemType itemType = entry.getKey().getItemType();
            SectionFunctionCode code = entry.getKey().getFunctionCode();
            Param param = entry.getKey().getParam();
            ListCounter listCounter = entry.getValue();

            Map<String, String> keyValues = Maps.newHashMap();
            keyValues.put("activity", activity.toString());
            keyValues.put("section", section.toString());
            keyValues.put("itemType", itemType.toString());
            keyValues.put("function", code.toString());
            keyValues.put("param", param.toString());
            keyValues.put("list", listCounter.list.toString());

            if (!DumpUtils.shouldAddToDump(filterMap, keyValues)) {
                continue;
            }
            String row = String.format(format, activity, section, itemType, code, param, listCounter.list,
                listCounter.call.get());
            rules.add(row);
        }

        return rules;
    }


    public List<String> dumpMissing() {

        final String format = "%-15s %-10s %-30s %-30s %-35s";

        List<String> rules = Lists.newArrayList();
        String header = String.format(format, "Activity", "Section", "ItemType", "Function", "Param");
        rules.add(header);

        for (MonoProductTitleKey key : this.monoProductMissingKeys) {

            ActivityType activity = key.getActivityType();
            SectionType section = key.getSectionType();
            ItemType itemType = key.getItemType();
            SectionFunctionCode code = key.getFunctionCode();
            Param param = key.getParam();

            String row = String.format(format, activity, section, itemType, code, param);
            rules.add(row);
        }

        return rules;
    }


    public TitleListEnum getTitleList(MonoProductTitleKey titleKey, ActionRecommendation action) {

        if (!this.monoProductsTitlesRules.containsKey(titleKey)) {

            this.monoProductMissingKeys.add(titleKey);
            action.addDebug("Title key not found: " + titleKey);
            return TitleListEnum.L3; // default title enum list
        }

        ListCounter listCounter = this.monoProductsTitlesRules.get(titleKey);
        listCounter.call.incrementAndGet();

        return listCounter.list;
    }


    /**
     * Set the dynamic title. For OFFER duplicated are not checked.
     */
    public void updateTitle(ActionRecommendation action, RuleSection ruleSection, List<Offer> offers) {

        if (offers != null) {
            for (Offer offer : offers) {
                this.updateTitle(action, ruleSection, offer);
            }
        }
    }


    /**
     * Set the dynamic title. For OFFER duplicated are not checked.
     */
    public void updateTitle(ActionRecommendation action, RuleSection ruleSection, Offer offer) {


        ItemType itemType = ItemType.getItemType(offer.getOffer().getClass());

        MonoProductTitleKey titleKey = new MonoProductTitleKey(action.getActivityType(), //
            SectionType.OFFER,//
            ruleSection.getFunction().getCode(), //
            ruleSection.getFunction().getParam(), //
            itemType); //


        TitleListEnum titleList = this.getTitleList(titleKey, action);
        Preconditions.checkNotNull(titleList);

        // For OFFER: we get the first title from the title list
        TitleEnum title = titleList.getTitles().get(0);

        action.getTitleData().setTitle(title);
        action.getTitleData().setOffer(ruleSection.getProduct());
        action.getTitleData().setOrigin(action.getOrigin());

        this.normalize(action.getTitleData(), ruleSection.getProduct());

        String destination = action.getTitleData().getDestinations().isEmpty() ? null : action.getTitleData()
            .getDestinations().iterator().next();
        Title t = TitleBuilder.builder(title)//
            .iata(destination)//
            .pr(ruleSection.getProduct())//
            .origin(action.getTitleData().getOrigin())//
            .build();//
        action.addTitleForHome(action.getCurrentHome(), title);
        if (action.getLanguage() != Language.UNKNOWN) {
            String titleDesc = this.i18nService.getI18nTitle(action.getTitleData(), action.getLanguage(),
                action.getCountryCode());
            t.setTitleDesc(titleDesc);
        }


        offer.setTitleOffer(t);

        action.addDebug("Title: " + t);
    }


    /**
     * Set the dynamic title. We try to avoid title duplicated in the rows.
     */
    public void updateTitle(ActionRecommendation action, RuleSection ruleSection, RowHome rowHome) {

        ItemType itemType = ItemType.getItemType(rowHome.getOffers().get(0).getClass());
        MonoProductTitleKey titleKey = new MonoProductTitleKey(action.getActivityType(), //
            SectionType.ROW,//
            ruleSection.getFunction().getCode(), //
            ruleSection.getFunction().getParam(), //
            itemType); //

        TitleListEnum titleList = this.getTitleList(titleKey, action);
        Preconditions.checkNotNull(titleList);

        TitleEnum title = this.getUniqueTitle(action, titleList);

        action.getTitleData().setTitle(title);
        action.getTitleData().setOffer(ruleSection.getProduct());
        action.getTitleData().setOrigin(action.getOrigin());

        this.normalize(action.getTitleData(), ruleSection.getProduct());
        String destination = action.getTitleData().getDestinations().isEmpty() ? null : action.getTitleData()
            .getDestinations().iterator().next();
        Title t = TitleBuilder.builder(title)//
            .iata(destination)//
            .pr(ruleSection.getProduct())//
            .origin(action.getTitleData().getOrigin())//
            .build();//


        if (action.getLanguage() != Language.UNKNOWN) {
            String titleDesc = this.i18nService.getI18nTitle(action.getTitleData(), action.getLanguage(),
                action.getCountryCode());
            t.setTitleDesc(titleDesc);
        }


        rowHome.getTitles().setMainTitle(t);

        action.addDebug("Title: " + t);

    }

    /**
     * Return iata cities for Hotels And Flights for title destinations
     */
    private void normalize(TitleData title, Product pr) {

        if (pr == Product.HOTELS || pr == Product.FLIGHTS) {
            if (title != null && title.getSingleDestination() != null) {
                title.addDestination(this.geoService.normalizeIata(title.getSingleDestination()));
            }
            if (title != null && title.getOrigin() != null) {
                title.setOrigin(this.geoService.normalizeIata(title.getOrigin()));
            }
        }
    }

    private TitleEnum getUniqueTitle(ActionRecommendation action, TitleListEnum list) {

        if (!action.getActivityType().equals(ActivityType.LAST_RSRT)) {
            Collections.shuffle(list.getTitles());
        }

        for (TitleEnum title : list.getTitles()) {
            if (!action.isTitleUsedForHome(action.getCurrentHome(), title)) {
                action.addTitleForHome(action.getCurrentHome(), title);
                return title;
            }
        }

        LOGGER.warn("All titles are being used for list: {}, userId: {}, ip: {}", list, action.getUserId(),
            action.getRequestIp());

        action.addDebug("All titles are being used for list: " + list);

        return list.getTitles().get(0);
    }


    private static class ListCounter {

        TitleListEnum list;
        AtomicInteger call;

        public ListCounter(TitleListEnum list) {
            this.list = list;
            this.call = new AtomicInteger();
        }

    }

    @VisibleForTesting
    public void setI18nService(I18nService i18nService) {
        this.i18nService = i18nService;
    }

    @VisibleForTesting
    public void setGeoService(GeoService geoService) {
        this.geoService = geoService;
    }

    public static void main(String[] args) {
        new TitleEngine().init();
    }

    public void updateTitle(ActionRecommendation action, List<Offer> offers) {

        if (offers != null) {
            for (Offer offer : offers) {
                this.updateTitle(action, offer);
            }
        }
    }

    public void updateTitle(ActionRecommendation action, Offer offer) {

        Product prod = ItemTypeId.getProduct(offer.getOffer().getOfferType());
        MultiProductTitleKey titleKey = new MultiProductTitleKey(action.getActivityType(), //
            SectionType.OFFER,//
            action.getTitleData().isSingleDestination(), //
            HomeSupport.getHomeSupport(prod));
        TitleListEnum titleList = this.getTitleList(titleKey, action);
        Preconditions.checkNotNull(titleList);

        // For OFFER: we get the first title from the title list
        TitleEnum title = titleList.getTitles().get(0);

        action.getTitleData().setTitle(title);
        action.getTitleData().setOffer(prod);
        this.updateTitleName(action.getTitleData());
        this.normalize(action.getTitleData(), prod);
        String destination = action.getTitleData().getDestinations().isEmpty() ? null : action.getTitleData()
            .getDestinations().iterator().next();
        Title t = TitleBuilder.builder(title)//
            .iata(destination)//
            .pr(prod)//
            .origin(action.getTitleData().getOrigin())//
            .build();//

        if (action.getLanguage() != Language.UNKNOWN) {
            String titleDesc = this.i18nService.getI18nTitle(action.getTitleData(), action.getLanguage(),
                action.getCountryCode());
            t.setTitleDesc(titleDesc);
        }
        offer.setTitleOffer(t);
        action.addDebug("Title: " + t);

    }

    private TitleListEnum getTitleList(MultiProductTitleKey titleKey, ActionRecommendation action) {
        if (!this.multiProductsTitlesRules.containsKey(titleKey)) {
            Preconditions.checkNotNull(this.multiProductsTitlesRules.containsKey(titleKey));
        }

        ListCounter listCounter = this.multiProductsTitlesRules.get(titleKey);
        listCounter.call.incrementAndGet();
        return listCounter.list;
    }

    public void updateTitle(ActionRecommendation action, RowHome row) {

        HomeSupport support = this.determineSupport(row.getOffers());

        MultiProductTitleKey titleKey = new MultiProductTitleKey(action.getActivityType(), //
            SectionType.ROW,//
            action.getTitleData().isSingleDestination(), //
            support);

        TitleListEnum titleList = this.getTitleList(titleKey, action);
        Preconditions.checkNotNull(titleList);

        TitleEnum title = this.getUniqueTitle(action, titleList);
        Product product = ItemTypeId.getProduct(row.getOffers().get(0).getOfferType());
        this.updateTitleName(action.getTitleData());
        action.getTitleData().setTitle(title);
        Title t;
        if (support.equals(HomeSupport.MAIN)) {
            product = null;
        } else {

            this.normalize(action.getTitleData(), product);

        }
        action.getTitleData().setOffer(product);
        action.getTitleData().setOrigin(action.getOrigin());
        String destination = action.getTitleData().getDestinations().isEmpty() ? null : action.getTitleData()
            .getDestinations().iterator().next();
        t = TitleBuilder.builder(title)//
            .iata(destination)//
            .pr(product)//
            .origin(action.getTitleData().getOrigin())//
            .build();

        if (action.getLanguage() != Language.UNKNOWN) {
            String titleDesc = this.i18nService.getI18nTitle(action.getTitleData(), action.getLanguage(),
                action.getCountryCode());
            t.setTitleDesc(titleDesc);
        }
        row.getTitles().setMainTitle(t);

        action.addDebug("Title: " + t);


    }

    private void updateTitleName(TitleData titleData) {

        Set<String> destinations = Sets.newHashSet();
        for (String destination : titleData.getDestinations()) {
            destinations.add(this.geoService.normalizeIata(destination));
        }
        titleData.setDestinations(destinations);
    }

    private HomeSupport determineSupport(List<? extends ItemHome> offers) {

        Product product = ItemTypeId.getProduct(offers.get(0).getOfferType());
        for (ItemHome itemHome : offers) {
            if (!ItemTypeId.getProduct(itemHome.getOfferType()).equals(product)) {
                return HomeSupport.MAIN;
            }
        }
        return HomeSupport.getHomeSupport(product);
    }
}
