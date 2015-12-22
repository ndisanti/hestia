package com.despegar.p13n.hestia.utils;

import com.newrelic.api.agent.NewRelic;

public final class NewRelicUtils {

    private NewRelicUtils() {
    }


    public static void incrementCounter(String name, int count) {
        if (count != 0) {
            NewRelic.incrementCounter(name, count);
        }
    }



}
