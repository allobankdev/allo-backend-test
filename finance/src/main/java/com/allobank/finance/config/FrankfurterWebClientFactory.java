package com.allobank.finance.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class FrankfurterWebClientFactory implements FactoryBean<WebClient> {

    private final FrankfurterProperties properties;

    public FrankfurterWebClientFactory(FrankfurterProperties properties) {
        this.properties = properties;
    }

    @Override
    public WebClient getObject() {
        return WebClient.builder()
                .baseUrl(properties.getBaseUrl())
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        return WebClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
