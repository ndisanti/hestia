package com.despegar.p13n.hestia.controller;

import java.net.InetAddress;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Language;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.HomeContent;
import com.despegar.p13n.hestia.api.data.model.HomeProduct;
import com.despegar.p13n.hestia.api.data.model.RulesVersion;
import com.despegar.p13n.hestia.exception.ApiIllegalArgumentException;
import com.despegar.p13n.hestia.external.geo.GeoService;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendationBuilder;
import com.despegar.p13n.hestia.recommend.allinone.HomeContentService;
import com.despegar.p13n.hestia.recommend.allinone.HomeParam;
import com.despegar.p13n.hestia.recommend.allinone.MultiObjecHomeVersion;
import com.despegar.p13n.hestia.recommend.allinone.rules.SectionFunctionEngine;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Function;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.Param;
import com.despegar.p13n.hestia.recommend.allinone.rules.section.SectionFunctionCode;
import com.despegar.p13n.hestia.recommend.allinone.title.TitleEngine;
import com.despegar.p13n.hestia.utils.ControllerUtils;
import com.despegar.p13n.hestia.utils.DumpUtils;
import com.despegar.p13n.hestia.utils.HestiaStringUtils;
import com.despegar.p13n.hestia.utils.ProductCountrySupportUtils;
import com.google.common.net.InetAddresses;
import com.newrelic.api.agent.NewRelic;

@RestController
public class HomeContentController {
		
	  	@Autowired
	    private HomeContentService homeContentService;

	    @Autowired
	    private GeoService geoService;

	    @Autowired
	    private TitleEngine titleEngine;

	    @Autowired
	    private SectionFunctionEngine functionEngine;

	    @Autowired
	    private ActionRecommendationBuilder actionBuilder;

	    @RequestMapping(value = "/v3/homecontent/cc/{cc}/ip/{ip}/id/{id}/lan/{lan}/", method = RequestMethod.GET)
	    public HomeContent getContentForAllProducts(
	        HttpServletRequest request,
	        @PathVariable("cc") String country,//
	        @PathVariable("ip") String ip, //
	        @PathVariable("id") String id, //
	        @PathVariable("lan") String language,//
	        @RequestParam(required = false, value = "debug") String debugStr,
	        @RequestParam(required = false, value = "trace") String traceStr,
	        @RequestParam(required = false, value = "forceRulesVersion") String forceRulesVersionStr,
	        @RequestParam(required = false, value = "forceMOVersion") String forceHomeVersionStr,
	        @RequestParam(required = false, value = "toTs") String toTsStr) {

	        ControllerUtils.checkLanguage(language);

	        return this.getHomeContent(country, ip, null, id, language, debugStr, traceStr, forceRulesVersionStr,
	            forceHomeVersionStr, toTsStr);
	    }

	    @RequestMapping(value = "/v3/homecontent/cc/{cc}/ip/{ip}/pr/{pr}/id/{id}/lan/{lan}/", method = RequestMethod.GET)
	    public HomeContent getContentForSingleProduct(HttpServletRequest request, @PathVariable("cc") String country,//
	        @PathVariable("ip") String ip, //
	        @PathVariable("pr") String productStr, //
	        @PathVariable("id") String id,//
	        @PathVariable("lan") String language,//
	        @RequestParam(required = false, value = "debug") String debugStr,//
	        @RequestParam(required = false, value = "trace") String traceStr,//
	        @RequestParam(required = false, value = "forceRulesVersion") String forceRulesVersionStr,//
	        @RequestParam(required = false, value = "forceMOVersion") String forceHomeVersionStr,//
	        @RequestParam(required = false, value = "toTs") String toTsStr) {

	        ControllerUtils.checkLanguage(language);

	        return this.getHomeContent(country, ip, productStr, id, language, debugStr, traceStr, forceRulesVersionStr,
	            forceHomeVersionStr, toTsStr);
	    }

	    private HomeContent getHomeContent(String country, String ip, String productStr, String id, String language,
	        String debugStr, String traceStr, String forceRulesVersionStr, String forceHomeVersionStr, String toTsStr) {

	        boolean debug = Boolean.valueOf(debugStr);
	        boolean trace = Boolean.valueOf(traceStr);
	        long startTs = System.currentTimeMillis();
	        NewRelic.addCustomParameter("timestamp", startTs);


	        HomeParam homeParam = this.getHomeParams(country, ip, productStr, id, language, debugStr, traceStr,
	            forceRulesVersionStr, toTsStr, debug, trace, forceHomeVersionStr);

	        HomeContent response = this.homeContentService.getContent(homeParam);

	        if (trace) {
	            long execTime = System.currentTimeMillis() - startTs;
	            homeParam.getTrace().setExecutionTimeMs(execTime);
	            response.setTrace(homeParam.getTrace());
	        }
	        if (debug) {
	            long execTime = System.currentTimeMillis() - startTs;
	            homeParam.debug("Execution time: " + execTime + " ms");
	            response.setDebug(homeParam.getDebug());
	        }
	        return response;
	    }

	    private HomeParam getHomeParams(String country, String ip, String productStr, String id, String language,
	        String debugStr, String traceStr, String forceRulesVersionStr, String toTsStr, boolean debug, boolean trace,
	        String forceHomeVersionStr) {

	        CountryCode cc = ControllerUtils.checkCountry(country);
	        Product pr = Product.fromString(productStr);
	        Language lan = ControllerUtils.checkLanguage(language);
	        InetAddress ipParsed;
	        RulesVersion forceVersion = validateVersion(forceRulesVersionStr);
	        MultiObjecHomeVersion homeVesion = this.validateHomeVersion(forceHomeVersionStr);

	        if (!HestiaStringUtils.isValidIPAddress(ip)) {
	            ipParsed = null;
	        } else {
	            ipParsed = InetAddresses.forString(ip);
	        }

	        if (id.equalsIgnoreCase("null")) {
	            id = null;
	        }

	        Long toTs = null;
	        if (toTsStr != null) {
	            toTs = Long.parseLong(toTsStr);
	        }


	        EnumSet<Product> homes;
	        if (pr == null) {
	            homes = ProductCountrySupportUtils.getHomesSupported(cc);
	        } else {
	            homes = EnumSet.of(pr);
	        }

	        return new HomeParam(cc, ipParsed, homes, id, lan, debug, trace, toTs, forceVersion, homeVesion);
	    }

	    private MultiObjecHomeVersion validateHomeVersion(String forceHomeVersionStr) {

	        if (HestiaStringUtils.isEmpty(forceHomeVersionStr)) {
	            return null;
	        }
	        MultiObjecHomeVersion version = MultiObjecHomeVersion.fromString(forceHomeVersionStr);
	        if (version == null) {
	            String errorMsg = String.format("Home version code not supported [%s]. Suported [%s]", forceHomeVersionStr,
	                MultiObjecHomeVersion.allNames());
	            throw new ApiIllegalArgumentException(errorMsg);
	        }
	        return version;
	    }

	    private static RulesVersion validateVersion(String forceRulesVersion) {
	        if (HestiaStringUtils.isEmpty(forceRulesVersion)) {
	            return null;
	        }
	        RulesVersion version = RulesVersion.fromString(forceRulesVersion);
	        if (version == null) {
	            String errorMsg = String.format("Rule engine version code not supported [%s]. Suported [%s]", forceRulesVersion,
	                RulesVersion.getAllNames());
	            throw new ApiIllegalArgumentException(errorMsg);
	        }
	        return version;
	    }

	    @RequestMapping(value = "/v3/homecontent/function/{function}/cc/{cc}/ip/{ip}/pr/{pr}/id/{id}/lan/{lan}/", method = RequestMethod.GET)
	    public HomeProduct getContentForFunction(
	        HttpServletRequest request,
	        @PathVariable("function") String function,//
	        @PathVariable("cc") String country,//
	        @PathVariable("ip") String ip,//
	        @PathVariable("pr") String productStr,//
	        @PathVariable("id") String id, //
	        @PathVariable("lan") String lan, //
	        @RequestParam(required = false, value = "debug") String debugStr,//
	        @RequestParam(required = false, value = "rankingType") String rankingType, //
	        @RequestParam(required = false, value = "carRankingType") String carRankingType, //
	        @RequestParam(required = false, value = "flow") String flow, //
	        @RequestParam(required = false, value = "crossPr") String crossPr, //
	        @RequestParam(required = false, value = "crossFlow2") String crossFlow2, //
	        @RequestParam(required = false, value = "seen") String seen, //
	        @RequestParam(required = false, value = "recommendType") String recommendType,
	        @RequestParam(required = false, value = "addSearch") String addSearch,
	        @RequestParam(required = false, value = "addBuy") String addBuy,
	        @RequestParam(required = false, value = "city") String city,
	        @RequestParam(required = false, value = "checkIsDetail") String checkIsDetail,
	        @RequestParam(required = false, value = "searchedDestination") String searchedDestination) {

	        boolean debug = Boolean.valueOf(debugStr);

	        Product pr = Product.fromString(productStr);
	        HomeParam homeParam = this.getHomeParams(country, ip, id, lan, debug);
	        ActionRecommendation action = this.actionBuilder.buildActionRecommendation(homeParam);
	        this.actionBuilder.populateUserContext(action);
	        Param param = new Param(rankingType, carRankingType, flow, crossPr, crossFlow2, seen, recommendType, addSearch,
	            addBuy, city, searchedDestination, checkIsDetail);

	        Function f = new Function(SectionFunctionCode.valueOf(function), param);
	        HomeProduct response = this.functionEngine.buildHomeProduct(f, pr, action);

	        return response;
	    }

	    private HomeParam getHomeParams(String country, String lan, String forceHomeVersionStr) {

	        MultiObjecHomeVersion homeVesion = this.validateHomeVersion(forceHomeVersionStr);
	        Language language = Language.fromString(lan);
	        CountryCode cc = ControllerUtils.checkCountry(country);
	        return new HomeParam(cc, language, homeVesion);
	    }

	    private HomeParam getHomeParams(String country, String ip, String id, String lan, boolean debug) {

	        InetAddress ipParsed;
	        if (!HestiaStringUtils.isValidIPAddress(ip)) {
	            ipParsed = null;
	        } else {
	            ipParsed = InetAddresses.forString(ip);
	        }

	        if (id.equalsIgnoreCase("null")) {
	            id = null;
	        }
	        Language language = Language.fromString(lan);
	        CountryCode cc = ControllerUtils.checkCountry(country);

	        return new HomeParam(cc, ipParsed, id, language, debug);
	    }

	    @RequestMapping(value = "/v3/homecontent/snapshot/cc/{cc}/lan/{lan}", method = RequestMethod.GET)
	    public HomeContent getSnaphost(//
	        @PathVariable("cc") String ccStr,//
	        @PathVariable("lan") String lanStr, //
	        @RequestParam(required = false, value = "forceHomeVersion") String forceHomeVersionStr) {

	        return this.getLastResortDefaultLan(ccStr, lanStr, forceHomeVersionStr);
	    }

	    @RequestMapping(value = "/v3/homecontent/lastResort/profile/dump", method = RequestMethod.GET)
	    public List<String> dumpProfiles(HttpServletRequest request) {

	        return this.homeContentService.dumpLastResortProfiles();
	    }

	    @RequestMapping(value = "/v3/homecontent/rules/dump", method = RequestMethod.GET)
	    public List<String> dumpRules(HttpServletRequest request,
	        @RequestParam(required = false, value = "separator") String separator,
	        @RequestParam(required = true, value = "rulesVersion") String rulesVersionStr) {

	        Map<String, String> filterMap = DumpUtils.getFilterMap(request.getParameterMap());
	        RulesVersion rulesVersion = validateVersion(rulesVersionStr);

	        return this.homeContentService.dumpRules(filterMap, separator, rulesVersion);
	    }

	    private HomeContent getLastResort(String ccStr, String lanStr, String forceHomeVersion) {
	        HomeParam homeParam = this.getHomeParams(ccStr, lanStr, forceHomeVersion);
	        return this.homeContentService.getLastResortContent(homeParam);
	    }

	    @RequestMapping(value = "/v3/homecontent/function/itemtype/dump", method = RequestMethod.GET)
	    public List<String> getFunctionItemTypeDump() {
	        return SectionFunctionCode.dumpItempTypes();
	    }

	    @RequestMapping(value = "/v3/homecontent/function/desc/dump", method = RequestMethod.GET)
	    public List<String> getFunctionDescriptionDump() {
	        return this.functionEngine.dumpFunctionDescriptions();
	    }

	    @RequestMapping(value = "/v3/homecontent/titlelist/rules/dump/multiproduct", method = RequestMethod.GET)
	    public List<String> getMultiProductTitlesListRulesDump(HttpServletRequest req) {

	        Map<String, String> filterMap = DumpUtils.getFilterMap(req.getParameterMap());

	        return this.titleEngine.dumpMultiProduct(filterMap);
	    }

	    @RequestMapping(value = "/v3/homecontent/titlelist/rules/dump/monoproduct", method = RequestMethod.GET)
	    public List<String> getMonoProductTitlesListRulesDump(HttpServletRequest req) {

	        Map<String, String> filterMap = DumpUtils.getFilterMap(req.getParameterMap());

	        return this.titleEngine.dumpMonoProduct(filterMap);
	    }

	    @RequestMapping(value = "/v3/homecontent/titlelist/rules/missing/dump", method = RequestMethod.GET)
	    public List<String> getTitlesListMissingRulesDump() {

	        return this.titleEngine.dumpMissing();
	    }

	    @RequestMapping(value = "/v3/homecontent/function/rules/dump", method = RequestMethod.GET)
	    public List<String> getFunctionListRulesDump(HttpServletRequest req) {

	        Map<String, String> filterMap = DumpUtils.getFilterMap(req.getParameterMap());

	        return this.functionEngine.dumpRules(filterMap);
	    }

	    @RequestMapping(value = "/v3/homecontent/lastresortdefault/cc/{cc}/lan/{lan}", method = RequestMethod.GET)
	    public HomeContent getLastResortDefaultLan(//
	        @PathVariable("cc") String ccStr,//
	        @PathVariable("lan") String lanStr,//
	        @RequestParam(required = false, value = "forceHomeVersion") String forceHomeVersionStr) {

	        ControllerUtils.checkLanguage(lanStr);

	        return this.getLastResort(ccStr, lanStr, forceHomeVersionStr);
	    }

	    @RequestMapping(value = "/v3/homecontent/rules/common/dump", method = RequestMethod.GET)
	    public List<String> getCommonRules(HttpServletRequest request,
	        @RequestParam(required = false, value = "separator") String separator) {

	        Map<String, String> filterMap = DumpUtils.getFilterMap(request.getParameterMap());

	        return this.homeContentService.dumpCommonRules(filterMap, separator);
	    }

	    @RequestMapping(value = "/v3/homecontent/hightraffic", method = RequestMethod.GET)
	    public Set<String> dumpHighTraffic(HttpServletRequest request) {

	        return this.homeContentService.dumpHighTraffic();
	    }
}
