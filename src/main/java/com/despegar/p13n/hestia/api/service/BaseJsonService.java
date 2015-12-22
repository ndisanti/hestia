package com.despegar.p13n.hestia.api.service;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.despegar.library.rest.utils.TypeReference;

/**
 * @author carlos
 *
 */
public abstract class BaseJsonService {

    @Autowired
    RestConnectorAdapter eulerRestConnector;

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    protected <T> T getForObject(final String url,
        final org.codehaus.jackson.type.TypeReference<EulerResponseContainer<T>> reference) {
        try {
            String response = this.eulerRestConnector.getBodyResponse(url);
            EulerResponseContainer<T> data = mapper.readValue(response, reference);
            if (data.hasErrors()) {
                throw new EulerConnectorException("Error calling service " + url, data.getErrors());
            }
            return data.getData();
        } catch (JsonParseException e) {
            throw new EulerConnectorException("Error parsing response", e);
        } catch (JsonMappingException e) {
            throw new EulerConnectorException("Error mapping response", e);
        } catch (IOException e) {
            throw new EulerConnectorException("Error connecting to server", e);
        }
    }

    protected <T> T getForObjectV3(final String url, final org.codehaus.jackson.type.TypeReference<T> reference) {
        try {
            String response = this.eulerRestConnector.getBodyResponse(url);
            T data = mapper.readValue(response, reference);
            return data;
        } catch (JsonParseException e) {
            throw new EulerConnectorException("Error parsing response", e);
        } catch (JsonMappingException e) {
            throw new EulerConnectorException("Error mapping response", e);
        } catch (IOException e) {
            throw new EulerConnectorException("Error connecting to server", e);
        }
    }

    protected <T> T getForObject(final String url, final TypeReference<EulerResponseContainer<T>> reference) {
        EulerResponseContainer<T> data = this.eulerRestConnector.getBodyResponse(url, reference);
        if (data.hasErrors()) {
            throw new EulerConnectorException("Error calling service " + url, data.getErrors());
        }
        return data.getData();
    }

    public <T> T postObject(String url, Object params, final TypeReference<EulerResponseContainer<T>> reference) {
        EulerResponseContainer<T> data = this.eulerRestConnector.postObject(url, params, reference);
        if (data.hasErrors()) {
            throw new EulerConnectorException("Error calling service " + url, data.getErrors());
        }
        return data.getData();
    }

    public void setEulerRestConnector(RestConnectorAdapter eulerRestConnector) {
        this.eulerRestConnector = eulerRestConnector;
    }

}
