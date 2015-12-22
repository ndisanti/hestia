package com.despegar.p13n.hestia.rest.interceptors;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.despegar.library.rest.interceptors.HttpRequestContext;
import com.despegar.library.rest.interceptors.HttpResponseContext;
import com.despegar.library.rest.interceptors.Interceptor;
import com.google.common.annotations.Beta;

@Beta
public class LoggingInterceptor
    extends Interceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    protected HttpResponseContext preHandle(HttpRequestContext request) throws IOException {
        LOGGER.debug("Request : " + request);
        return super.preHandle(request);
    }
}
