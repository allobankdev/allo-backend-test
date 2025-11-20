package com.nurmaya.allobank.idr_rate_aggregator.configuration;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class FrankfurterClientFactory implements FactoryBean<WebClient>{
    @Autowired
    private FrankfurterProperties frankfurterProperties;

    @Override
    public WebClient getObject()  {
        return WebClient.builder()
                .baseUrl(frankfurterProperties.getBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        return WebClient.class;
    }
}
