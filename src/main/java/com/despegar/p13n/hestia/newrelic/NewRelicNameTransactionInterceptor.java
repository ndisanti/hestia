package com.despegar.p13n.hestia.newrelic;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.newrelic.api.agent.NewRelic;

public class NewRelicNameTransactionInterceptor
    extends HandlerInterceptorAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(NewRelicNameTransactionInterceptor.class);

    public NewRelicNameTransactionInterceptor() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        final String bestMatchingPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);

        String handlerName;

        // to handle cglib proxies
        if (Enhancer.isEnhanced(handler.getClass())) {
            handlerName = handler.getClass().getSuperclass().getSimpleName();
        } else {
            handlerName = handler.getClass().getSimpleName();
        }

        if (bestMatchingPattern != null) {
            NewRelic.setTransactionName(handlerName, bestMatchingPattern);
            LOG.debug("setTransactionName({},{})", handlerName, bestMatchingPattern);
        }

        final Map<String, String> uriTemplateVariables = (Map<String, String>) request
            .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        if (uriTemplateVariables != null) {
            for (Entry<String, String> entry : uriTemplateVariables.entrySet()) {
                NewRelic.addCustomParameter(entry.getKey(), entry.getValue());
                LOG.debug("addCustomParameter({},{})", entry.getKey(), entry.getValue());
            }
        }

        response.setHeader("X-Service", bestMatchingPattern);

        return true;
    }
}
