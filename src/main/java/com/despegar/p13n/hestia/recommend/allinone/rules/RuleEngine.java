package com.despegar.p13n.hestia.recommend.allinone.rules;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import com.despegar.p13n.euler.commons.client.model.Product;
import com.despegar.p13n.hestia.api.data.model.HomeContent;
import com.despegar.p13n.hestia.api.data.model.HomeProduct;
import com.despegar.p13n.hestia.api.data.model.RulesVersion;
import com.despegar.p13n.hestia.recommend.allinone.ActionRecommendation;
import com.newrelic.api.agent.Trace;

public interface RuleEngine {

    public void start();

    List<String> dumpRulesAsString(Map<String, String> filterMap, String separator, RulesVersion rulesVersion);

    EnumSet<RulesVersion> supportedVersions();

    void checkRanges();

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
    HomeContent buildHomeContent(ActionRecommendation action);

    HomeProduct buildHomeForProduct(ActionRecommendation action, Product home);

}
