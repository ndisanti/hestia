package com.despegar.p13n.hestia.api.service;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import com.despegar.library.rest.HttpResponse;
import com.despegar.library.rest.HttpStatus;
import com.despegar.library.rest.RestConnector;
import com.despegar.library.rest.builder.HttpRequestBuilder;
import com.despegar.library.rest.utils.TypeReference;

public class RestConnectorAdapter {

    private RestConnector connector;

    public RestConnectorAdapter(RestConnector connector) {
        super();
        this.connector = connector;
    }

    public String getBodyResponse(String url) {
        HttpRequestBuilder builder = this.connector.get(url).accept("application/json");
        try {
            HttpResponse response = builder.execute();
            if (response.getStatus() == HttpStatus.OK) {
                return response.getBody();
            } else {
                String msg = "Error on http GET: " + url + ". Status: " + response.getStatus() + ". Body: "
                    + response.getBody();
                throw new EulerConnectorException(msg);
            }
        } catch (IOException e) {
            throw new EulerConnectorException("Error on http GET: " + url, e);
        }
    }

    public String getBodyResponse(String url, Map<String, String> headers) {
        HttpRequestBuilder builder = this.connector.get(url).accept("application/json");

        for (Entry<String, String> entry : headers.entrySet()) {
            builder = builder.withHeader(entry.getKey(), entry.getValue());
        }

        try {
            HttpResponse response = builder.execute();
            if (response.getStatus() == HttpStatus.OK) {
                return response.getBody();
            } else {
                String msg = "Error on http GET: " + url + ". Status: " + response.getStatus() + ". Body: "
                    + response.getBody();
                throw new EulerConnectorException(msg);
            }
        } catch (IOException e) {
            throw new EulerConnectorException("Error on http GET: " + url, e);
        }
    }

    public <T> T getBodyResponse(String url, TypeReference<T> typeRef) {
        HttpRequestBuilder builder = this.connector.get(url).accept("application/json");
        try {
            HttpResponse response = builder.execute();
            if (response.getStatus() == HttpStatus.OK) {
                return response.getBodyAs(typeRef);
            } else {
                String msg = "Error on http GET: " + url + ". Status: " + response.getStatus() + ". Body: "
                    + response.getBody();
                throw new EulerConnectorException(msg);
            }
        } catch (IOException e) {
            throw new EulerConnectorException("Error on http GET: " + url, e);
        }
    }

    public <T> T postObject(String url, Object params, TypeReference<T> typeRef) {

        try {
            HttpResponse response = this.connector.post(url).withBody(params).asContentType("application/json").execute();

            if (response.getStatus() == HttpStatus.OK) {
                return response.getBodyAs(typeRef);
            } else {
                String msg = "Error on http POST: " + url + ". Status: " + response.getStatus() + ". Body: "
                    + response.getBody();
                throw new EulerConnectorException(msg);
            }
        } catch (IOException e) {
            throw new EulerConnectorException("Error on http POST: " + url, e);
        }

    }

}
