package com.despegar.p13n.hestia.newrelic;

import java.util.Collections;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.newrelic.api.agent.NewRelic;

public class NewRelicHeaderAttributeInterceptor
    extends HandlerInterceptorAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(NewRelicHeaderAttributeInterceptor.class);

    private Set<String> headerKeys;

    public NewRelicHeaderAttributeInterceptor() {
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        for (String headerKey : this.headerKeys == null ? Collections.<String> emptySet() : this.headerKeys) {
            String value = request.getHeader(headerKey);
            if (value != null) {
                NewRelic.addCustomParameter(headerKey, value);
                LOG.debug("addCustomParameter({},{})", headerKey, value);
            }
        }
        return super.preHandle(request, response, handler);
    }


    public void setHeaderKeys(Set<String> headerKeys) {
        this.headerKeys = headerKeys;
    }



}
