package com.allo.test.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class WebClientFactoryBean implements FactoryBean<WebClient> {

    @Override
    public WebClient getObject() {
        return WebClient.builder()
                .baseUrl("https://api.frankfurter.app")
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        return WebClient.class;
    }
}