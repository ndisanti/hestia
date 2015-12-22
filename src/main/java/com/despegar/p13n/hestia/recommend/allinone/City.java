package com.despegar.p13n.hestia.recommend.allinone;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;


public enum City {

    ORLANDO("ORL"), ANY();

    private static Map<String, City> dictFromString = new HashMap<String, City>();
    static {
        for (City type : City.values()) {
            for (String cityCode : type.values) {
                dictFromString.put(cityCode.toLowerCase(), type);
            }
            dictFromString.put(type.name().toLowerCase(), type);
        }
    }

    public List<String> getValues() {
        return this.values;
    }

    private final List<String> values;

    private City(final String... values) {
        this.values = Arrays.asList(values);
    }

    public static City fromString(final String cityCode) {
        return dictFromString.get(StringUtils.defaultString(cityCode).toLowerCase());
    }

}
