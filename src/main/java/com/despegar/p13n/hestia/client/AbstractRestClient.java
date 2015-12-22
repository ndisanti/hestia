package com.despegar.p13n.hestia.client;

import java.io.IOException;

import com.despegar.library.rest.HttpResponse;
import com.despegar.library.rest.HttpStatus;
import com.despegar.library.rest.Query;
import com.despegar.library.rest.RestConnector;
import com.despegar.library.rest.builder.HttpRequestBuilder;
import com.despegar.library.rest.utils.TypeReference;
import com.newrelic.api.agent.Trace;

public abstract class AbstractRestClient {

    protected RestConnector restConnector;


    public void setRestConnector(RestConnector restConnector) {
        this.restConnector = restConnector;
    }

    @Trace
    protected <T> T execute(final String url, TypeReference<T> typeRef) {
        HttpRequestBuilder builder = this.restConnector.get(url);
        try {
            return this.execute(typeRef, builder);
        } catch (IOException e) {
            throw new RuntimeException("Error on http GET: " + url, e);
        }
    }


    @Trace
    protected <T> T execute(Query query, TypeReference<T> typeRef) {
        HttpRequestBuilder builder = this.restConnector.get(query);
        try {
            return this.execute(typeRef, builder);
        } catch (IOException e) {
            throw new RuntimeException("Error on http GET: " + query.toString(), e);
        }
    }


    private <T> T execute(TypeReference<T> typeRef, HttpRequestBuilder builder) throws IOException {
        HttpResponse response = builder.execute();
        if (response.getStatus() == HttpStatus.OK) {
            return response.getBodyAs(typeRef);
        } else {
            throw new RuntimeException(response.getBody());
        }
    }



}
