package com.despegar.p13n.hestia.recommend.allinone.rules;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Language;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.HomeContent;
import com.despegar.p13n.hestia.api.data.model.HomeProduct;
import com.despegar.p13n.hestia.api.data.model.RulesVersion;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.activity.UserActivity;
import com.google.common.base.Preconditions;
import com.newrelic.api.agent.Trace;

/**
 * <p>Rule engine to build home content</p>
 * 
 * <p>Supports many rules versions</p>
 */
public class EngineFacade {

    protected static final Logger LOG = LoggerFactory.getLogger(EngineFacade.class);


    private static final String CHECK_IN = "ci";

    private static final String DEPARTURE_MONTH = "dm";

    private static final String DEPARTURE_TIME = "dt";

    private SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");

    private SimpleDateFormat cruiseFormater = new SimpleDateFormat("MM/yyyy");

    private SimpleDateFormat responseFormater = new SimpleDateFormat("dd/MM");

    private final EnumMap<RulesVersion, RuleEngine> engines = new EnumMap<>(RulesVersion.class);

    private final HomeContentValidator validator;

    private volatile boolean started = false;

    private LastResortEngine lastResortEngine;

    private NoHistoryEngine noHistoryEngine;

    public EngineFacade(List<RuleEngine> enginesList, LastResortEngine lastResortEngine, NoHistoryEngine noHistoryEngine,
        HomeContentValidator validator) {

        for (RuleEngine engine : enginesList) {
            for (RulesVersion rv : engine.supportedVersions()) {
                Preconditions.checkState(!this.engines.containsKey(rv), "Rules version engine: %s already added", rv);
                this.engines.put(rv, engine);
            }
        }
        this.lastResortEngine = lastResortEngine;
        this.noHistoryEngine = noHistoryEngine;
        this.validator = validator;
    }

    public void start() {
        LOG.info("Home content: starting engines...");
        for (RuleEngine engine : this.engines.values()) {
            engine.start();
        }
        this.started = true;
        LOG.info("Home content: engines started.");
    }

    public void checkRanges() {
        for (RuleEngine engine : this.engines.values()) {
            engine.checkRanges();
        }
    }

    /**
     * <p>Builds home content with a fallback mechanism:</p>
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
            action.setCurrentHome(home);
            action.resetFunctionState();

            action.addDebug("-------------------- Building content for: " + home + " --------------------");
            HomeProduct homeProduct = this.getEngine(action).buildHomeForProduct(action, home);

            // if a single home cant be built we default
            if (homeProduct != null) {
                content.addProduct(home, homeProduct);
                this.validator.validate(home, content, action);
            }
        }
        content.setLastActivity(action.getActivityType().name());
        this.determineCheckinDate(action, content);
        return content;
    }

    private void determineCheckinDate(ActionRecommendation action, HomeContent content) {

        switch (action.getActivityType()) {
        case BUY:
            this.determineCheckinDate(action.getBuyActivity().getActivity(), content);
            break;
        case SEARCH:
            this.determineCheckinDate(action.getSearchActivity().getLastActivity(), content);
            break;
        default:
            break;
        }
    }

    private void determineCheckinDate(UserActivity activity, HomeContent content) {
        String field = "";
        switch (activity.getProduct()) {
        case CRUISES:
            field = DEPARTURE_MONTH;
            break;
        case CARS:
            field = DEPARTURE_TIME;
            break;
        default:
            field = CHECK_IN;
            break;
        }
        String ci = activity.getAction().getCollectionFromActionMap(field).toString();
        ci = ci.replace("[", "").replace("]", "");
        if (!ci.isEmpty()) {
            try {
                Date date = activity.getProduct().equals(Product.CRUISES) ? this.cruiseFormater.parse(ci) : this.formater
                    .parse(ci);
                if (date.after(new Date())) {
                    content.setCheckIn(this.responseFormater.format(date));
                }
            } catch (ParseException e) {
                LOG.error(e.getMessage());
            }
        }
    }

    public List<String> dumpRulesAsString(Map<String, String> filterMap, String separator, RulesVersion rulesVersion) {
        return this.getEngine(rulesVersion).dumpRulesAsString(filterMap, separator, rulesVersion);
    }

    public List<String> dumpLastResortRulesAsString(Map<String, String> filterMap, String separator) {
        return this.lastResortEngine.dumpLastResortRulesAsString(filterMap, separator);
    }

    private RuleEngine getEngine(ActionRecommendation action) {
        return this.getEngine(action.getVersion());
    }

    private RuleEngine getEngine(RulesVersion version) {
        return this.engines.get(version);
    }

    public HomeProduct getLastResortDefault(Product homeToCheck, CountryCode cc, Language lan) {
        return this.lastResortEngine.getLastResortDefault(homeToCheck, cc, lan);
    }

    public List<String> dumpNoHistoryRulesAsString(Map<String, String> filterMap, String separator) {
        return this.noHistoryEngine.dumpRulesAsString(filterMap, separator);
    }
}
