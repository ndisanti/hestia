package com.despegar.p13n.hestia.recommend.allinone;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.despegar.p13n.hestia.api.data.model.HomeContent;
import com.despegar.p13n.hestia.api.data.model.RulesVersion;

public interface HomeContentService {

    HomeContent getContent(HomeParam homeParam);

    /**
     * Dump rules for SEARCH and BUY for a given rule version
     */
    List<String> dumpRules(Map<String, String> filterMap, String separator, RulesVersion rulesVersion);

    /**
     * Dump no history and last resort rules
     */
    List<String> dumpCommonRules(Map<String, String> filterMap, String separator);

    HomeContent getLastResortContent(HomeParam homeParam);

    List<String> dumpLastResortProfiles();

    Set<String> dumpHighTraffic();
}
