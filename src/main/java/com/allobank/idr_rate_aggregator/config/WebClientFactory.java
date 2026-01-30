package com.allobank.idr_rate_aggregator.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class WebClientFactory implements FactoryBean<WebClient> {

    @Value("${frankfurter.api.url}")
    private String apiUrl;

    @Override
    public WebClient getObject() throws Exception {
        return WebClient.builder()
                .baseUrl(apiUrl)
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        return WebClient.class;
    }

}
