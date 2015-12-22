package com.despegar.p13n.hestia.rest.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.despegar.p13n.commons.newrelic.MetricNameGenerator;
import com.newrelic.api.agent.NewRelic;

/**
 * Interceptor that checks for X-Client header.
 * 
 * @author jonatan
 *
 */
public class XClientInterceptor
    extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(XClientInterceptor.class);

    private static String[] allowedPaths = new String[] {"manage", "health-check", "version"};
    private static final String XCLIENT_HEADER = "X-Client";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String xClient = request.getHeader(XCLIENT_HEADER);
        // si es un request publico, ignoro (lo maneja el api agent o la app)
        if (request.getHeader("X-Api-Public") == null) {
            String userAgent = request.getHeader("User-Agent");
            if (userAgent != null) {
                // http://en.wikipedia.org/wiki/User_agent#Format
                if (!(userAgent.startsWith("Mozilla") || userAgent.startsWith("curl")) && xClient == null) {
                    if (!this.isDefaultAllowed(request.getPathInfo())) {
                        NewRelic.incrementCounter(MetricNameGenerator.xClientMetric());
                        // throw new ApiException(new ApiError(EulerApiErrorCode.XCLIENT_EXCEPTION,
                        // "The X-Client header was not present in the request " + request.getPathInfo()
                        // + " for user agent " + userAgent));
                    }
                }
            } else if (xClient == null) {
                if (!this.isDefaultAllowed(request.getPathInfo())) {
                    NewRelic.incrementCounter(MetricNameGenerator.xClientMetric());
                    // throw new ApiException(new ApiError(EulerApiErrorCode.XCLIENT_EXCEPTION,
                    // "The X-Client header was not present in the request " + request.getPathInfo() +
                    // " for user agent "
                    // + userAgent));
                }
            }
        }
        logger.debug("Request: (" + ((xClient == null) ? "unknown" : xClient) + ") -> " + request.getMethod() + " "
            + request.getRequestURI());
        return true;
    }

    private boolean isDefaultAllowed(String pathInfo) {
        for (String allowedPath : allowedPaths) {
            if (pathInfo.contains(allowedPath)) {
                return true;
            }
        }
        return false;
    }

}
