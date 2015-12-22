package com.despegar.p13n.hestia;


import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.despegar.library.rest.RestConnector;
import com.despegar.library.rest.RestConnectorFactory;
import com.despegar.library.rest.config.RestConnectorConfig;
import com.despegar.library.rest.serializers.json.JsonConfig;
import com.despegar.p13n.hestia.api.service.RestConnectorAdapter;
import com.despegar.p13n.hestia.api.service.external.PublicCarsService;
import com.despegar.p13n.hestia.service.CarsServiceImpl;

/**
 * Beans configuration
 * 
 * @author jonatan
 *
 */
@Configuration
public class ApiConfig {

    @Value("${rest.euler.url}")
    private String endpointUrl;

    @Value("${rest.euler.clientId}")
    private String client;

    @Value("${rest.euler.version}")
    private String version;

    @Value("${rest.euler.readTimeout:1000}")
    private int readTimeout;

    @Value("${rest.euler.connectionTimeout:1000}")
    private int connectionTimeout;

    @Value("${rest.euler.maxConnections:300}")
    private int maxconnections;

    @Bean
    public RestConnector eulerRestConnector() {

        URL endpoint;
        try {
            endpoint = new URL(this.endpointUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        RestConnectorConfig connectorConfig = RestConnectorConfig.createBuilder().maxConnections(this.maxconnections)
            .readTimeout(this.readTimeout).connectionTimeout(this.connectionTimeout)
            .jsonConfig(JsonConfig.createBuilder().withCamelCaseFormat().build()).build();

        String host = endpoint.getPort() == -1 ? endpoint.getHost() : endpoint.getHost() + ":" + endpoint.getPort();

        RestConnector connector = RestConnectorFactory.createRestConnector("http", host, endpoint.getPath(), true,
            this.client, this.version, connectorConfig);
        return connector;
    }

    @Bean
    public RestConnectorAdapter connectorAdapter() {
        return new RestConnectorAdapter(this.eulerRestConnector());
    }

   
    @Bean
    public PublicCarsService carsService() {
        return new CarsServiceImpl();
    }

   
}
