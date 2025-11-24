package com.test.allo_bank_test_exhange_rate.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component("frankfurterWebClientFactory")
public class WebClientFactoryBean implements FactoryBean<WebClient> {
    
    private final FrankFurtherProperties frankFurtherProperties;

    public WebClientFactoryBean(FrankFurtherProperties frankFurtherProperties) {
        this.frankFurtherProperties = frankFurtherProperties;
    }

    @Override
    public WebClient getObject() {
        return WebClient.builder()
                .baseUrl(frankFurtherProperties.getBaseUrl())
                .defaultHeader("Accept", "application/json")
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
