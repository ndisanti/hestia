package com.despegar.p13n.hestia.recommend.allinone;

import java.net.InetAddress;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Language;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.euler.commons.client.model.UserContext;
import com.despegar.p13n.euler.commons.client.model.UserLocation;
import com.despegar.p13n.euler.commons.client.model.WishList;
import com.despegar.p13n.euler.commons.client.model.data.ProductData;
import com.despegar.p13n.hestia.api.data.model.HomeContentTrace;
import com.despegar.p13n.hestia.api.data.model.RulesVersion;
import com.despegar.p13n.hestia.api.data.model.home.ItemTypeId;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.activity.Anticipation;
import com.despegar.p13n.hestia.recommend.allinone.activity.BuyActivity;
import com.despegar.p13n.hestia.recommend.allinone.activity.LastAction;
import com.despegar.p13n.hestia.recommend.allinone.activity.SearchActivity;
import com.despegar.p13n.hestia.recommend.allinone.rules.RuleContentPriority;
import com.despegar.p13n.hestia.recommend.allinone.rules.RuleItem;
import com.despegar.p13n.hestia.recommend.allinone.rules.RuleSection;
import com.despegar.p13n.hestia.recommend.allinone.rules.SectionsEnum;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Current;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Function;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.itemcontent.HomeRuleContent;
import com.despegar.p13n.hestia.recommend.allinone.rules.tree.sectioncontent.SectionRuleContent;
import com.despegar.p13n.hestia.recommend.allinone.title.SectionType;
import com.despegar.p13n.hestia.recommend.allinone.title.TitleData;
import com.despegar.p13n.hestia.recommend.allinone.title.TitleEnum;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;

public class ActionRecommendation {


   	// url path parameters
    private String userId;
    private EnumSet<Product> homes;
    private CountryCode countryCode;
    private Language language;
    private InetAddress ip;
    private RulesVersion forceRulesversion;
    private MultiObjecHomeVersion forceHomeVersion;
    private Map<SectionsEnum, Set<String>> sectionsDestinations = Maps.newHashMap();


    // optional params
    private RulesVersion version;

    private Product noHistoryRecommendedHome;

    private UserContext userContext;

    // null if it was not calculated
    private Boolean isCountryIpValid;

    // unique item id home, offer product and type
    private SetMultimap<UniqueIdKey, ItemIdSection> uniqueId = HashMultimap.create();

    // titles being used for each home product. Used to avoid duplicated titles
    private SetMultimap<Product, TitleEnum> titlesByProduct = HashMultimap.create();

    // unique function by home and offer. Used for dynamic services
    private Set<UniqueFunctionKey> uniqueFunction = Sets.newHashSet();

    private String origin;

    // null if the user didn't buy
    private BuyActivity buyActivity;

    // null if has no search history
    private SearchActivity searchActivity;

    private HomeContentTrace traceData;

    private Current current = new Current();

    private boolean lastResort;

    private HomeParam homeParam;

    public ActionRecommendation(String userId, Product home, CountryCode countryCode, Language language, InetAddress ip,
        RulesVersion version, RulesVersion foreceRulesversion, MultiObjecHomeVersion forveHomeVersion) {
        this(userId, EnumSet.of(home), countryCode, language, ip, version, foreceRulesversion, forveHomeVersion);
    }

    public ActionRecommendation(HomeParam homeParam) {

        this(homeParam.getUserId(), homeParam.getHome(), homeParam.getCc(), homeParam.getLan(), homeParam.getIp(), homeParam
            .getForceRulesVersion(), homeParam.getForceMOVersion());

        if (homeParam.getDebug() != null) {
            homeParam.debug(homeParam.toString());
        }

        this.homeParam = homeParam;
    }

    private ActionRecommendation(String userId, EnumSet<Product> homes, CountryCode countryCode, Language language,
        InetAddress ip, RulesVersion version, RulesVersion forceRulesVersion, MultiObjecHomeVersion forceHomeVersion) {
        super();
        Preconditions.checkNotNull(language);
        this.userId = userId;
        this.homes = homes;
        this.countryCode = countryCode;
        this.language = language;
        this.ip = ip;
        this.version = version;
        this.forceRulesversion = forceRulesVersion;
        this.setForceHomeVersion(forceHomeVersion);
        this.initializeMap();
    }


    private ActionRecommendation(String userId, EnumSet<Product> homes, CountryCode cc, Language lan, InetAddress ip,
        RulesVersion forceRulesVersion, MultiObjecHomeVersion forceHomeVersion) {
        super();
        Preconditions.checkNotNull(lan);
        this.userId = userId;
        this.homes = homes;
        this.countryCode = cc;
        this.language = lan;
        this.ip = ip;
        this.forceRulesversion = forceRulesVersion;
        this.setForceHomeVersion(forceHomeVersion);
        this.initializeMap();
    }

    private void initializeMap() {
        this.sectionsDestinations.put(SectionsEnum.ROW1, new HashSet<String>());
        this.sectionsDestinations.put(SectionsEnum.ROW2, new HashSet<String>());
        this.sectionsDestinations.put(SectionsEnum.ROW3, new HashSet<String>());
    }

    public void setIsCountryIpValid(Boolean isCountryIpValid) {
        this.isCountryIpValid = isCountryIpValid;
    }

    public String getUserId() {
        return this.userId;
    }

    public EnumSet<Product> getHomes() {
        return this.homes;
    }

    public CountryCode getCountryCode() {
        return this.countryCode;
    }

    @VisibleForTesting
    public void setCountryCode(CountryCode countryCode) {
        this.countryCode = countryCode;
    }

    public Language getLanguage() {
        return this.language;
    }

    public InetAddress getRequestIp() {
        return this.ip;
    }

    public String getOrigin() {
        return this.origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public boolean addDestination(Product home, Product offer, String destination) {
        // Preconditions.checkNotNull(home); may be null for when defaulting
        Preconditions.checkNotNull(offer);
        Preconditions.checkNotNull(destination);


        return this.uniqueId.put(new UniqueIdKey(home, offer, ItemTypeId.DESTINATION),
            new ItemIdSection(this.getCurrentSection(), destination));
    }

    public boolean addCarCategory(Product home, Product offer, String destination, String carcat) {
        // Preconditions.checkNotNull(home); may be null when defaulting
        Preconditions.checkNotNull(offer);
        Preconditions.checkNotNull(destination);
        Preconditions.checkNotNull(carcat);

        return this.uniqueId.put(new UniqueIdKey(home, offer, ItemTypeId.CAR_CATEGORY),
            new ItemIdSection(this.getCurrentSection(), destination + "-" + carcat));
    }

    public boolean addRegion(Product home, Product offer, String region) {
        // Preconditions.checkNotNull(home); may be null when defaulting
        Preconditions.checkNotNull(offer);
        Preconditions.checkNotNull(region);
        return this.uniqueId.put(new UniqueIdKey(home, offer, ItemTypeId.REGION), new ItemIdSection(
            this.getCurrentSection(), region));
    }

    public boolean addDid(Product home, Product offer, String did) {
        // Preconditions.checkNotNull(home); may be null when defaulting
        Preconditions.checkNotNull(offer);
        Preconditions.checkNotNull(did);
        return this.uniqueId.put(new UniqueIdKey(home, offer, ItemTypeId.DID), new ItemIdSection(this.getCurrentSection(),
            did));
    }

    public boolean addCluid(Product home, Product offer, String cluid) {
        // Preconditions.checkNotNull(home); may be null when defaulting
        Preconditions.checkNotNull(offer);
        Preconditions.checkNotNull(cluid);
        return this.uniqueId.put(new UniqueIdKey(home, offer, ItemTypeId.CLUID), new ItemIdSection(this.getCurrentSection(),
            cluid));
    }

    public boolean addHid(Product home, Product offer, String hid) {
        // Preconditions.checkNotNull(home); may be null when defaulting
        Preconditions.checkNotNull(offer);
        Preconditions.checkNotNull(hid);
        return this.uniqueId.put(new UniqueIdKey(home, offer, ItemTypeId.HID), new ItemIdSection(this.getCurrentSection(),
            hid));
    }

    public boolean addVrid(Product home, Product offer, String vrid) {
        Preconditions.checkNotNull(offer);
        Preconditions.checkNotNull(vrid);
        return this.uniqueId.put(new UniqueIdKey(home, offer, ItemTypeId.VRID), new ItemIdSection(this.getCurrentSection(),
            vrid));
    }

    public boolean addActid(Product home, Product offer, String actid) {
        // Preconditions.checkNotNull(home); may be null when defaulting
        Preconditions.checkNotNull(offer);
        Preconditions.checkNotNull(actid);
        return this.uniqueId.put(new UniqueIdKey(home, offer, ItemTypeId.ACTID), new ItemIdSection(this.getCurrentSection(),
            actid));
    }

    public BuyActivity getBuyActivity() {
        return this.buyActivity;
    }

    public void setBuyActivity(BuyActivity buyActivity) {
        this.buyActivity = buyActivity;
    }

    public SearchActivity getSearchActivity() {
        return this.searchActivity;
    }

    public void setSearchActivity(SearchActivity searchActivity) {
        this.searchActivity = searchActivity;
    }

    public Boolean getIsCountryIpValid() {
        return this.isCountryIpValid;
    }

    public ActivityType getActivityType() {

        if (this.getBuyActivity() != null) {
            return ActivityType.BUY;
        }

        if (this.getSearchActivity() != null) {
            return ActivityType.SEARCH;
        }

        return this.lastResort ? ActivityType.LAST_RSRT : ActivityType.NO_HISTORY;
    
   
    }

    public Product getActivityProduct(Product pr) {
        ActivityType activityType = this.getActivityType();

        switch (activityType) {
        case BUY:
            return this.buyActivity.getProduct();

        case SEARCH:
            return this.searchActivity.getActivityOrLast(pr).getAction().getProduct();

        case LAST_RSRT:
        case NO_HISTORY:
            return null;

        default:
            throw new IllegalArgumentException("ActivityType: " + activityType);
        }
    }

    public Flow getActivityFlow(Product pr) {
        ActivityType activityType = this.getActivityType();

        switch (activityType) {
        case BUY:
            return this.buyActivity.getActivity().getFlow();

        case SEARCH:
            return this.searchActivity.getActivityOrLast(pr).getFlow();

        case LAST_RSRT:
        case NO_HISTORY:
            return null;

        default:
            throw new IllegalArgumentException("ActivityType: " + activityType);
        }
    }


    /**
     * Return the destination for the specific activity
     */
    public String getLastDestination() {
        ActivityType activityType = this.getActivityType();

        switch (activityType) {
        case BUY:
            return this.buyActivity.getActivity().getDestination();

        case SEARCH:
            return this.searchActivity.getLastActivity().getDestination();

        case NO_HISTORY:
        case LAST_RSRT:
            return null;

        default:
            throw new IllegalArgumentException("ActivityType: " + activityType);
        }
    }

    /**
     * Return the destination for the specific activity
     */
    public String getDestination(Product pr) {
        ActivityType activityType = this.getActivityType();

        switch (activityType) {
        case BUY:
            return this.buyActivity.getActivity().getDestination();

        case SEARCH:
            return this.searchActivity.getActivityOrLast(pr).getDestination();

        case NO_HISTORY:
        case LAST_RSRT:
            return null;

        default:
            throw new IllegalArgumentException("ActivityType: " + activityType);
        }
    }


    public VisitFlow getVisitFlow(Product offer) {

        if (this.getSearchActivity() == null) {
            return VisitFlow.ANY;
        } else {
            if (this.getSearchActivity().isDetailOrCheckoutFor(offer)) {
                return VisitFlow.DETAIL;
            } else {
                return VisitFlow.ANY;
            }
        }
    }

    public int getLastActionDays(Product pr) {
        if (this.getBuyActivity() != null) {
            return this.getBuyActivity().getActivity().getLastActionDays();
        }

        if (this.getSearchActivity() != null) {
            return this.getSearchActivity().getActivityOrLast(pr).getLastActionDays();
        }

        return LastAction.any();
    }

    public int getLastActionDays() {
        if (this.getBuyActivity() != null) {
            return this.getBuyActivity().getActivity().getLastActionDays();
        }

        if (this.getSearchActivity() != null) {
            return this.getSearchActivity().getLastActivity().getLastActionDays();
        }

        return LastAction.any();
    }


    public int getAnticipationDays(Product pr) {
        if (this.getBuyActivity() != null) {
            return this.getBuyActivity().getActivity().getAnticipationDays();
        }

        if (this.getSearchActivity() != null) {
            return this.getSearchActivity().getActivityOrLast(pr).getAnticipationDays();
        }

        return Anticipation.any();
    }

    public int getAnticipationDays() {
        if (this.getBuyActivity() != null) {
            return this.getBuyActivity().getActivity().getAnticipationDays();
        }

        if (this.getSearchActivity() != null) {
            return this.getSearchActivity().getLastActivity().getAnticipationDays();
        }

        return Anticipation.any();
    }

    public UserLocation getHomeUserLocation() {
        return this.userContext.getUserLocation();
    }

    public boolean isDebug() {
        return this.homeParam.getDebug() != null;
    }

    public void addDebug(String debug) {
        if (this.homeParam != null) {
            this.homeParam.debug(debug);
        }
    }

    public void addDebug(SectionsEnum section, RuleSection ruleSection) {
        this.addDebug(section + "=" + ruleSection.toString());
    }

    public void addDebug(SectionsEnum section, HomeRuleContent homeRuleContent) {
        this.addDebug(section + "=" + homeRuleContent.toString());
    }

    public void addDebug(SectionsEnum section, List<RuleItem> ruleItemList) {
        this.addDebug(section + "=" + this.asStringList(ruleItemList));
    }

    public void addDebug(SectionRuleContent sectionRuleContent) {
        if (this.homeParam != null) {
            this.addDebug("---");
            for (SectionsEnum section : SectionsEnum.ORIGINAL_SECTIONS) {
                this.addDebug(section + ": " + sectionRuleContent.get(section));
            }
        }
    }

    public void addDebug(HomeRuleContent homeRuleContent) {
        if (this.homeParam != null) {

            int i = 0;

            this.addDebug(SectionsEnum.values()[i++] + ":" + this.asStringList(homeRuleContent.getOffers()));

            for (List<RuleItem> row : homeRuleContent.getRowList()) {
                this.addDebug(SectionsEnum.values()[i++] + ": " + this.asStringList(row));
            }
        }
    }

    private List<String> asStringList(List<RuleItem> ruleItemList) {
        List<String> offersStr = Lists.newArrayList();

        for (RuleItem item : ruleItemList) {
            offersStr.add(item.toDebug());
        }
        return offersStr;
    }


    public void addDebug(RuleContentPriority ruleContentPriority) {
        if (this.homeParam != null) {
            this.addDebug("---");
            for (SectionsEnum section : SectionsEnum.ORIGINAL_SECTIONS) {
                this.addDebug(section + "=" + ruleContentPriority.get(section));
            }
        }
    }

    public RulesVersion getVersion() {
        return this.version;
    }

    public void setVersion(RulesVersion version) {
        this.version = version;
    }

    public SectionsEnum getCurrentSection() {
        return this.current.section;
    }

    public void setCurrentSection(SectionsEnum currentSection) {
        this.current.section = currentSection;
    }


    public TitleData getTitleData() {
        return this.current.titleData;
    }

    public void setTitleData(TitleData titleData) {
        this.current.titleData = titleData;
    }


    public TitleData getSubtitleHighlightData() {
        return this.current.subtitleHighlightData;
    }

    public void setSubtitleHighlightData(TitleData subtitleHighlightData) {
        this.current.subtitleHighlightData = subtitleHighlightData;
    }

    public TitleData getSubtitleOfferData() {
        return this.current.subtitleOfferData;
    }

    public void setSubtitleOfferData(TitleData subtitleOfferData) {
        this.current.subtitleOfferData = subtitleOfferData;
    }

    /**
     * Clear items ids that are being used by a section that was not completed and discarded.
     */
    public void releaseUniqueIds(Product home, Product product, SectionsEnum section) {
        Iterator<Entry<UniqueIdKey, ItemIdSection>> it = this.uniqueId.entries().iterator();

        while (it.hasNext()) {
            Entry<UniqueIdKey, ItemIdSection> entry = it.next();

            UniqueIdKey unique = entry.getKey();

            if (unique.getHome().equals(home) && unique.getOffer().equals(product)) {
                if (entry.getValue().getSection() == section) {
                    it.remove();
                }
            }
        }
    }

    public void releaseUniqueIds(Product home, SectionsEnum section) {
        Iterator<Entry<UniqueIdKey, ItemIdSection>> it = this.uniqueId.entries().iterator();

        while (it.hasNext()) {
            Entry<UniqueIdKey, ItemIdSection> entry = it.next();

            UniqueIdKey unique = entry.getKey();

            if (unique.getHome().equals(home)) {
                if (entry.getValue().getSection() == section) {
                    it.remove();
                }
            }
        }
    }

    public List<ProductData> getHistory() {
    	return this.userContext.getProductDataList();
    }

    public Product getCurrentHome() {
        return this.current.currentHome;
    }

    public void setCurrentHome(Product currentHome) {
        this.current.currentHome = currentHome;
    }

    public WishList getWishList() {
    	return this.userContext.getWishlist();
    }


    public void addTitleForHome(Product home, TitleEnum title) {
        this.titlesByProduct.put(home, title);
    }

    public boolean isTitleUsedForHome(Product home, TitleEnum title) {
        Preconditions.checkNotNull(home);
        Preconditions.checkNotNull(title);
        return this.titlesByProduct.containsEntry(home, title);
    }

    public void markFunctionAsBeingUsed(SectionType sectionType, Product home, Product offer, Function function) {

        // we only check for function replication for Dynamic Service
        if (this.version != RulesVersion.DYNAMIC_SERVICE) {
            return;
        }

        Preconditions.checkNotNull(sectionType);
        Preconditions.checkNotNull(home);
        Preconditions.checkNotNull(offer);
        Preconditions.checkNotNull(function);
        this.uniqueFunction.add(new UniqueFunctionKey(sectionType, home, offer, function));
    }

    public boolean isFunctionBeingUsed(SectionType sectionType, Product home, Product offer, Function function) {

        // we only check for function duplication for Dynamic Service and when is not build the last resort
        if (this.lastResort || this.version != RulesVersion.DYNAMIC_SERVICE) {
            return false;
        }

        Preconditions.checkNotNull(sectionType);
        Preconditions.checkNotNull(home);
        Preconditions.checkNotNull(offer);
        Preconditions.checkNotNull(function);
        return this.uniqueFunction.contains(new UniqueFunctionKey(sectionType, home, offer, function));
    }

    public boolean isLastResort() {
        return this.lastResort;
    }

    public void setLastResort(boolean lastResort) {
        this.lastResort = lastResort;
    }

    public FunctionPrecalc getFunctionPrecalc() {
        return this.current.functionPrecalc;
    }

    public void resetFunctionState() {
        this.current.functionPrecalc = new FunctionPrecalc();
    }

    public void addtoDebug() {

        StringBuilder sb = new StringBuilder();

        sb.setLength(0);
        sb.append("Rule version: ").append(this.getVersion());
        this.addDebug(sb.toString());

        sb.setLength(0);
        sb.append("Search Activity: ");
        if (this.getSearchActivity() == null) {
            sb.append("null");
        }
        this.addDebug(sb.toString());


        if (this.getSearchActivity() != null) {
            for (String searchRow : this.getSearchActivity().toStringListForDebug()) {
                sb.setLength(0);
                sb.append("  " + searchRow);
                this.addDebug(sb.toString());
            }

        }


        sb.setLength(0);
        sb.append("Last Activity: ").append(
            this.getSearchActivity() == null ? null : this.getSearchActivity().getLastActivity());
        this.addDebug(sb.toString());

        sb.setLength(0);
        sb.append("Buy Activity: ").append(this.getBuyActivity());
        this.addDebug(sb.toString());

    }

    public void addtoTrace() {
        if (this.traceData != null) {
            this.traceData.setRuleVersion(this.getVersion());
            this.traceData.setSearchActivity(this.getSearchActivity() != null ? this.getSearchActivity()
                .toStringListForDebug() : null);
            this.traceData.setBuyActivity(this.getBuyActivity() != null ? this.getBuyActivity().toString() : null);
        }
    }

    public HomeContentTrace getHomeContentTrace() {
        return this.traceData;
    }

    @Override
    public String toString() {
        return "ActionRecommendation [userId=" + this.userId + ", product=" + this.homes + ", countryCode="
            + this.countryCode + ", ip=" + this.ip + ", isCountryIpValid=" + this.isCountryIpValid + "]";
    }

    public RulesVersion getForceRulesversion() {
        return this.forceRulesversion;
    }

    public void setForceRulesversion(RulesVersion forceRulesversion) {
        this.forceRulesversion = forceRulesversion;
    }

    public void addLastDestinationTrace() {
        if (this.getHomeContentTrace() != null) {
            this.getHomeContentTrace().setLastDestinations(this.getSearchActivity().getLastDestinations());
        }

    }

    public void setMabTrace(String string) {
        if (this.getHomeContentTrace() != null) {
            this.getHomeContentTrace().setMab(string);
        }
    }

    public MultiObjecHomeVersion getForceHomeVersion() {
        return this.forceHomeVersion;
    }

    public void setForceHomeVersion(MultiObjecHomeVersion forceHomeVersion) {
        this.forceHomeVersion = forceHomeVersion;
    }

    public Map<String, Integer> getSearchDestination() {
        return this.userContext.getSearchDestinationCounter();
    }

    public Map<SectionsEnum, Set<String>> getSectionsDestinations() {
        return this.sectionsDestinations;
    }

    public void setSectionsDestinations(Map<SectionsEnum, Set<String>> sectionsDestinations) {
        this.sectionsDestinations = sectionsDestinations;
    }

    public Product getNoHistoryRecommendedHome() {
        return this.noHistoryRecommendedHome;
    }

    public void setNoHistoryRecommendedHome(Product noHistoryRecommendedHome) {
        this.noHistoryRecommendedHome = noHistoryRecommendedHome;
    }

    public HomeParam getHomeParam() {
        return this.homeParam;
    }

	public void setUserContext(UserContext userContext) {
		this.userContext = userContext;
	}
	 public UserContext getUserContext() {
			return userContext;
	 }
}
