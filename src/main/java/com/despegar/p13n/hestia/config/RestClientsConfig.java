package com.despegar.p13n.hestia.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.despegar.library.rest.RestConnector;
import com.despegar.library.rest.config.RestConnectorConfig;
import com.despegar.library.rest.interceptors.impl.DespegarHeadersInterceptor;
import com.despegar.library.rest.serializers.json.JsonConfig;
import com.despegar.p13n.euler.commons.client.UserContextClient;
import com.despegar.p13n.hestia.client.BuyMatrixCounterClient;
import com.despegar.p13n.hestia.client.DestinationsClient;
import com.despegar.p13n.hestia.client.RankingsClient;
import com.despegar.p13n.hestia.client.RecommendationsClient;
import com.despegar.p13n.hestia.external.hrm.HRMClient;
import com.despegar.p13n.hestia.rest.interceptors.AccessTokenInterceptor;
import com.despegar.p13n.hestia.rest.interceptors.LoggingInterceptor;

/**
 * All rest clients configuration
 *
 */
@Configuration
public class RestClientsConfig {

    private String CLIENT_VERSION = "1.0.0";

    @Value("${service.client:hestia}")
    private String client;

    private String protocol = "http";

    @Value("${euler.endpoint:backoffice.despegar.it}")
    private String eulerHost;


    @Value("${geo.service.host:geo.despegar.com}")
    private String geoHost;

    @Value("${hotels.service.v3.endpoint:hotels.despegar.it}")
    private String hotelsHost = "10.2.7.11";
    
    @Value("${api.v3.token:216vemllgpvf1ekvh4nb7097sv}")
    private String appToken;


    @Bean
    public RankingsClient rankingsClient() {
        RankingsClient client = new RankingsClient();
        client.setRestConnector(this.createCamelCaseRestConnector(this.eulerHost));
        return client;
    }

    @Bean
    public RecommendationsClient recommendationsClient() {
        RecommendationsClient client = new RecommendationsClient();
        client.setRestConnector(this.createCamelCaseRestConnector(this.eulerHost));
        return client;
    }

    @Bean
    public DestinationsClient destinationsClient() {
    	DestinationsClient client = new DestinationsClient();
        client.setRestConnector(this.createCamelCaseRestConnector(this.eulerHost));
        return client;
    }
    
    
    @Bean(name = "geoRestConnector")
    public RestConnector geoRestConnector() {
        RestConnectorConfig connectorConfig = RestConnectorConfig.createBuilder().readTimeout(180 * 1000)
            .connectionTimeout(3000).maxConnections(20).setShutdownHookEnabled(true).withValidateAfterInactivity(5000)
            .jsonConfig(JsonConfig.createBuilder().withSnakeCaseFormat().build()).build();
        return this.createRestConnector(connectorConfig, this.geoHost);
    }

    @Bean
    public BuyMatrixCounterClient buyMatrixCounterClient() {
    	BuyMatrixCounterClient client = new BuyMatrixCounterClient();
        client.setRestConnector(this.createCamelCaseRestConnector(this.eulerHost));
        return client;
    }

    @Bean(name = "hrmClientV3")
    public HRMClient hrmClientV3() {
        HRMClient hrmClient = new HRMClient();
        RestConnector restConnector = this.createSnakeCaseRestConnector(this.hotelsHost);
        restConnector.getInterceptors().add(new AccessTokenInterceptor(this.appToken));//
        hrmClient.setRestConnector(restConnector);
        return hrmClient;
    }
    
    @Bean
    public UserContextClient userContextClient() {
        return new UserContextClient(this.createCamelCaseRestConnector(this.eulerHost));
    }

    private RestConnector createCamelCaseRestConnector(String host) {
        RestConnectorConfig config = RestConnectorConfig.createBuilder().maxConnections(20).setShutdownHookEnabled(true)
            .withValidateAfterInactivity(5000).jsonConfig(JsonConfig.createBuilder().withCamelCaseFormat().build()).build();
        return this.createRestConnector(config, host);
    }

    private RestConnector createSnakeCaseRestConnector(String host) {
        RestConnectorConfig config = RestConnectorConfig.createBuilder().maxConnections(20).setShutdownHookEnabled(true)
            .withValidateAfterInactivity(5000).jsonConfig(JsonConfig.createDefault()).build();
        return this.createRestConnector(config, host);
    }

    private RestConnector createRestConnector(RestConnectorConfig config, String host) {
        RestConnector restConnector = RestConnector.createDespegarRestConnector(this.protocol, host, config);
        restConnector.getInterceptors().addFirst(new DespegarHeadersInterceptor(this.client, this.CLIENT_VERSION))
            .add(new LoggingInterceptor());
        return restConnector;
    }
}
