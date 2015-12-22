package com.despegar.p13n.hestia.rest.interceptors;

import java.io.IOException;

import com.despegar.library.rest.interceptors.HttpRequestContext;
import com.despegar.library.rest.interceptors.HttpResponseContext;
import com.despegar.library.rest.interceptors.Interceptor;

/**
 * 
 * @author nachokk
 *
 */
public class AccessTokenInterceptor
    extends Interceptor {


    private static final String X_ACCESS_TOKEN = "X-Access-Token";

    private final String accessToken;

    public AccessTokenInterceptor(String accessToken) {
        this.accessToken = accessToken;
    }


    @Override
    protected HttpResponseContext preHandle(HttpRequestContext request) throws IOException {
        if (this.accessToken != null) {
            request.getHeaders().set(X_ACCESS_TOKEN, this.accessToken);
        }
        return this.next(request);
    }

}
