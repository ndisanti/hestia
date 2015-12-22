package com.despegar.p13n.hestia.utils;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;

public class DumpUtils {

    /**
     * for printing: i.e. COMBINED_PRODUCTS returns COMBINED
     */
    public static String dumpProduct(String a) {

        int index = a.indexOf('_');

        if (index > 0) {
            return a.substring(0, index);
        }

        return a.substring(0, Math.min(a.length(), 8));
    }

    public static boolean shouldAddToDump(Map<String, String> filterMap, Map<String, String> keyValues) {

        for (Entry<String, String> entry : keyValues.entrySet()) {
            if (filterMap.containsKey(entry.getKey()) && //
                !StringUtils.containsIgnoreCase(entry.getValue(), filterMap.get(entry.getKey()))) {
                return false;
            }
        }

        return true;
    }

    public static Map<String, String> getFilterMap(Map<String, String[]> paramMap) {

        Map<String, String> filterMap = Maps.newHashMap();

        for (Entry<String, String[]> entry : paramMap.entrySet()) {
            filterMap.put(entry.getKey(), entry.getValue()[0]);
        }
        return filterMap;

    }
}
