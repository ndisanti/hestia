package com.despegar.p13n.hestia.newrelic;

import java.util.Map;

import com.google.common.cache.Cache;

/**
 * Cache Reporting feature.
 * 
 * <p>Services implementing this interface will report cache stats automatically.
 * 
 * <p>Dashboard url : https://rpm.newrelic.com/accounts/66629/custom_dashboards/31/pages/23117
 *
 * @see {@link CacheMetricReporter}
 * @author ejoncas
 * @since Sep 25, 2013
 */
public interface CacheReporting {

    /**
     * Gets the caches for reporting.
     * 
     * This method use rawtypes to allow returning different types of cache on the same method.
     * 
     * @return the caches for reporting
     */
    @SuppressWarnings("rawtypes")
    Map<String, Cache> getCachesForReporting();

}
