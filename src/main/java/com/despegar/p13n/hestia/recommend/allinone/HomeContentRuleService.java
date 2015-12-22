package com.despegar.p13n.hestia.recommend.allinone;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.despegar.p13n.commons.filter.HighTrafficFilterService;
import com.despegar.p13n.commons.newrelic.MetricNameGenerator;
import com.despegar.p13n.commons.newrelic.NewRelicNotifier;
import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.HomeContent;
import com.despegar.p13n.hestia.api.data.model.HomeProduct;
import com.despegar.p13n.hestia.api.data.model.RulesVersion;
import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.despegar.p13n.hestia.recommend.allinone.rules.CountrySupport;
import com.despegar.p13n.hestia.recommend.allinone.rules.EngineFacade;
import com.despegar.p13n.hestia.service.MabHomeService;
import com.despegar.p13n.hestia.utils.ProductCountrySupportUtils;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;

/**
 * Service for home content based on rules.
 */
@Service
public class HomeContentRuleService
    implements HomeContentService, NewRelicNotifier {

    protected static final Logger log = LoggerFactory.getLogger(HomeContentRuleService.class);

    public static final EnumSet<Product> ALL_HOMES = EnumSet.of(Product.FLIGHTS, Product.CARS, Product.CRUISES,
        Product.HOTELS, Product.CLOSED_PACKAGES, Product.HOME_AS_PRODUCT, Product.ACTIVITIES, Product.INSURANCE,
        Product.VACATIONRENTALS);

    @Autowired
    @Qualifier("homeExecutorService")
    private ExecutorService homeAsyncExecutorService;

    @Autowired
    private SearchBoxRecommender searchBoxRecommender;

    @Autowired
    private EngineFacade engineFacade;

    @Autowired
    private ActionRecommendationBuilder actionBuilder;

    @Value("${homecontent.timeout.enable:true}")
    private boolean handleTimeOut;

    @Value("${homecontent.timeout.millis:300}")
    private int millisToTimeout;

    private AtomicInteger requests = new AtomicInteger(0);
    private AtomicInteger timeouts = new AtomicInteger(0);
    private AtomicInteger errors = new AtomicInteger(0);
    private AtomicInteger lastResort = new AtomicInteger(0);

    private AtomicInteger highTrafficIpCount = new AtomicInteger(0);
    private AtomicInteger highTrafficuserIdCount = new AtomicInteger(0);
    private AtomicInteger emptyUserId = new AtomicInteger(0);

    @Autowired
    private MabHomeService mabHomeService;

    @Autowired
    private ProfilesService lastResortProfilesService;

    private HighTrafficFilterService highTrafficIp = new HighTrafficFilterService(100, 15, TimeUnit.MINUTES);

    private HighTrafficFilterService highTrafficUserid = new HighTrafficFilterService(100, 1, TimeUnit.HOURS);

    @Trace
    public HomeContent getContent(HomeParam homeParam) {
        if (homeParam.getIp() == null && null == homeParam.getUserId()) {
            return this.getLastResortContent(homeParam);
        } else if (homeParam.getUserId() != null && homeParam.getUserId().isEmpty()) {
            this.emptyUserId.incrementAndGet();
            return this.getLastResortContent(homeParam);
        } else {
            return this.getHomeContent(homeParam);
        }
    }

    private HomeContent getHomeContent(HomeParam homeParam) {
    	
        this.requests.incrementAndGet();


        Preconditions.checkArgument(
            ProductCountrySupportUtils.getHomesSupported(homeParam.getCc()).containsAll(homeParam.getHome()),
            " Home product is not supported: " + homeParam.getHome());


        boolean ipHighTraffic = homeParam.getIp() != null
            && this.highTrafficIp.shouldFilter(homeParam.getIp().getHostAddress());
        boolean userIdHighTraffic = homeParam.getUserId() != null
            && this.highTrafficUserid.shouldFilter(homeParam.getUserId());

        if (ipHighTraffic) {
            this.highTrafficIpCount.incrementAndGet();

            homeParam.debug("Warning!. High traffic detected for ip address. Returning last resort");
            return this.getLastResortContent(homeParam);
        }


        if (userIdHighTraffic) {
            this.highTrafficuserIdCount.incrementAndGet();

            homeParam.debug("Warning!. High traffic detected for user id. Returning last resort");
            return this.getLastResortContent(homeParam);
        }


        homeParam.debug("Building content for " + homeParam.getHome());

        HomeContent homeContent = null;

        if (this.handleTimeOut) {
            homeParam.debug("Handling timeout");
            homeContent = this.getContentAsync(homeParam);

        } else {
            homeParam.debug("No handling timeout");
            homeContent = this.getContentSync(homeParam);
        }

        // at this point could be a completed home, a timeout or error.

        homeContent = homeContent == null ? new HomeContent() : homeContent;
        this.checkMissingHomes(homeParam, homeContent);
        this.setSingleOrMultiProduct(homeParam.getHome(), homeContent);
        return homeContent;
    }

    private void checkMissingHomes(HomeParam homeParam, final HomeContent content) {
        Set<Product> builtHomes = content.getProducts().keySet();
        boolean lastResort = false;

        for (Product homeToCheck : homeParam.getHome()) {
            if (!builtHomes.contains(homeToCheck)) {
                lastResort = true;

                homeParam.debug("Getting last resort content for " + homeToCheck);

                // if the home is not supported in that country we build the home but not count as last resort
                if (ProductCountrySupportUtils.isSupported(homeParam.getCc(), homeToCheck)) {
                    homeParam.addLastResortTrace(homeToCheck);
                }
                HomeProduct homeProduct = this.engineFacade.getLastResortDefault(homeToCheck, homeParam.getCc(),
                    homeParam.getLan());
                content.addProduct(homeToCheck, homeProduct);
            }
        }

        if (lastResort) {
            // one lastResort count per request
            this.lastResort.incrementAndGet();
        }
    }


    private void registerIncompleteHome(HomeParam homeParam, final HomeContent content, ActionRecommendation action) {
        Set<Product> builtHomes = content.getProducts().keySet();

        for (Product homeToCheck : homeParam.getHome()) {
            if (!builtHomes.contains(homeToCheck)) {
                this.registerLastResort(homeParam, homeToCheck, action.getVersion(), action.getActivityType());
            } else {
                this.registerNoLastResort(homeParam, homeToCheck, action.getVersion(), action.getActivityType());
            }
        }
    }

    private void registerNoLastResort(HomeParam homeParam, Product homeToCheck, RulesVersion version, ActivityType activity) {
        this.lastResortProfilesService.registerProfile(homeParam, homeToCheck, version, false, activity);
    }


    private void registerLastResort(HomeParam homeParam, Product homeToCheck, RulesVersion version, ActivityType activity) {
        this.lastResortProfilesService.registerProfile(homeParam, homeToCheck, version, true, activity);
    }

    public List<String> dumpLastResortProfiles() {
        return this.lastResortProfilesService.dumpLastResortProfiles();
    }

    private void setSingleOrMultiProduct(EnumSet<Product> homes, HomeContent content) {
        if (homes.size() == 1) {
            content.setSingleProduct(true);
        } else {
            content.setSingleProduct(false);
        }
    }

    public HomeContent getLastResortContent(HomeParam homeParam) {


        boolean replaceCC = false;
        CountryCode newCC = null;

        if (!CountrySupport.RELEVANTS_CC.contains(homeParam.getCc())) {
            newCC = CountryCode.GB;
            replaceCC = true;
        } else {
            newCC = homeParam.getCc();
        }

        HomeContent content = new HomeContent();

        if (replaceCC) {
            homeParam.debug("Last resort Country Code: " + homeParam.getCc() + " replaced with " + newCC);
        }

        for (Product home : ProductCountrySupportUtils.getHomesSupported(newCC)) {
            HomeProduct homeProduct = this.engineFacade.getLastResortDefault(home, newCC, homeParam.getLan());
            content.addProduct(home, homeProduct);
        }
        content.setUserLocation("");
        content.setLastActivity(ActivityType.LAST_RSRT.name());
        return content;
    }

    /**
     * @param cc
     * @param ip
     * @param homePr
     * @param id
     * @param version
     * @param debugData
     * @param toTs
     */
    @Trace
    private HomeContent getContentAsync(final HomeParam homeParam) {
        Callable<HomeContent> task = new Callable<HomeContent>() {

            @Trace(dispatcher = true)
            public HomeContent call() {
                NewRelic.setTransactionName("HomeContentRuleService", "HomeContentRuleService.getContentSync");
                return HomeContentRuleService.this.getContentSync(homeParam);
            }
        };

        Future<HomeContent> future = this.homeAsyncExecutorService.submit(task);
        try {
            return future.get(this.millisToTimeout, TimeUnit.MILLISECONDS);

        } catch (TimeoutException ex) {
            homeParam.setTimeouted(true);
            homeParam.setTimeOutTrace(true);
            homeParam.debug("Timeout!. Getting cached last resort home content.");
            // defaulteo
            this.timeouts.incrementAndGet();
        } catch (InterruptedException e) {
            NewRelic.noticeError(e);
            log.error("Error on submiting event", e);
            homeParam.debug("InterruptedException: " + ExceptionUtils.getStackTrace(e.getCause()));
            homeParam.setErrorTrace(true);
            this.errors.incrementAndGet();
        } catch (ExecutionException e) {
            NewRelic.noticeError(e.getCause());
            log.error("Error on submiting event", e);
            homeParam.debug("Exception Thrown. " + e.getCause() + ": " + e.getMessage());
            homeParam.debug("ExecutionException: " + ExceptionUtils.getStackTrace(e.getCause()));
            homeParam.setErrorTrace(true);
            this.errors.incrementAndGet();
        }
        return null;
    }

    /**
     * @param cc
     * @param ip
     * @param homePr
     * @param id
     * @param version
     */
    private void addNewrelicErrorParams(HomeParam homeParam, RulesVersion version) {
        NewRelic.addCustomParameter("ruleVersion", version == null ? null : version.toString());
        NewRelic.addCustomParameter("cc", homeParam.getCc().name());
        NewRelic.addCustomParameter("ip", homeParam.getIp() == null ? null : homeParam.getIp().getHostAddress());
        NewRelic.addCustomParameter("pr", homeParam.getHome().toString());
        NewRelic.addCustomParameter("id", homeParam.getUserId());
        NewRelic.addCustomParameter("lan", homeParam.getLan() == null ? null : homeParam.getLan().name());
        NewRelic.addCustomParameter("debugData", homeParam.getDebug() == null ? null : homeParam.getDebug().toString());
        NewRelic.addCustomParameter("ts", System.currentTimeMillis());
    }


    /**
    * Build home content making a blocking call
    */
    private HomeContent getContentSync(HomeParam homeParam) {

        // For snaphost: we force to get the last resort default

        if (homeParam.getUserId() == null && homeParam.getIp() == null) {
            if (homeParam.getDebug() != null) {
                homeParam.getDebug().add("UserId and ip are null, so getting last resort default for snapshot");
            }
            return null;
        }

        ActionRecommendation action = this.actionBuilder.buildActionRecommendation(homeParam);

        this.actionBuilder.populateUserContext(action);

        try {
            HomeContent homeContent = this.callEngineUsingMab(action);
            boolean isAppInstalled = action.getUserContext().getUserRecord().isMobileAppInstalled();
            homeContent.setAppMobileInstalled(isAppInstalled);
            this.searchBoxRecommender.recommendHome(homeContent, action);

            if (action.getUserContext() == null || action.getUserContext().getUserLocation() == null) {
                homeContent.setUserLocation("");
            } else {
                homeContent.setUserLocation(action.getUserContext().getUserLocation().getCity());
            }


            this.registerIncompleteHome(homeParam, homeContent, action);

            return homeContent;

        } catch (Exception e) {

            this.addNewrelicErrorParams(homeParam, action.getVersion());
            NewRelic.noticeError(e);
            log.error("Error on submiting event", e);
            homeParam.setErrorTrace(true);
            this.errors.incrementAndGet();

            if (homeParam.getDebug() != null) {
                homeParam.getDebug().add("Exception thrown: " + ExceptionUtils.getStackTrace(e));
            }

            return null;
        }
    }

    private HomeContent callEngineUsingMab(ActionRecommendation action) {
        return this.mabHomeService.callEngine(action);
    }

    public List<String> dumpRules(Map<String, String> filterMap, String separator, RulesVersion rulesVersion) {
        return this.engineFacade.dumpRulesAsString(filterMap, separator, rulesVersion);
    }

    @VisibleForTesting
    public void setEngineFacade(EngineFacade engineFacade) {
        this.engineFacade = engineFacade;
    }

    @VisibleForTesting
    public void setActionBuilder(ActionRecommendationBuilder actionBuilder) {
        this.actionBuilder = actionBuilder;
    }

    @VisibleForTesting
    public void setHandleTimeOut(boolean handleTimeOut) {
        this.handleTimeOut = handleTimeOut;
    }

    @VisibleForTesting
    public void setMillisToTimeout(int millisToTimeout) {
        this.millisToTimeout = millisToTimeout;
    }

    @VisibleForTesting
    public void setHomeAsyncExecutorService(ExecutorService homeAsyncExecutorService) {
        this.homeAsyncExecutorService = homeAsyncExecutorService;
    }

    @VisibleForTesting
    public void setMabhomeService(MabHomeService mabHomeService) {
        this.mabHomeService = mabHomeService;
    }

    public void notifyNewRelic() {
        NewRelic.incrementCounter(MetricNameGenerator.serviceMetric("homecontent", "request", "total"),
            this.requests.getAndSet(0));
        NewRelic.incrementCounter(MetricNameGenerator.serviceMetric("homecontent", "request", "timeout"),
            this.timeouts.getAndSet(0));
        NewRelic.incrementCounter(MetricNameGenerator.serviceMetric("homecontent", "request", "error"),
            this.errors.getAndSet(0));
        NewRelic.incrementCounter(MetricNameGenerator.serviceMetric("homecontent", "request", "lastResort"),
            this.lastResort.getAndSet(0));
        NewRelic.incrementCounter(MetricNameGenerator.serviceMetric("homecontent", "request", "highTrafficIp"),
            this.highTrafficIpCount.getAndSet(0));
        NewRelic.incrementCounter(MetricNameGenerator.serviceMetric("homecontent", "request", "highTrafficId"),
            this.highTrafficuserIdCount.getAndSet(0));
        NewRelic.incrementCounter(MetricNameGenerator.serviceMetric("homecontent", "request", "emptyUser"),
            this.emptyUserId.getAndSet(0));
    }

    public List<String> dumpCommonRules(Map<String, String> filterMap, String separator) {

        List<String> noHistory = this.engineFacade.dumpNoHistoryRulesAsString(filterMap, separator);
        List<String> lastResort = this.engineFacade.dumpLastResortRulesAsString(filterMap, separator);
        List<String> all = Lists.newArrayList(noHistory);
        all.addAll(lastResort);
        return all;
    }

    public Set<String> dumpHighTraffic() {

        Set<String> ids = new HashSet<String>();
        ids.addAll(this.highTrafficIp.blackListIds());
        ids.addAll(this.highTrafficUserid.blackListIds());
        return ids;

    }
}