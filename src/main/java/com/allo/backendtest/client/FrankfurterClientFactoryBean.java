package com.allo.backendtest.client;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Component
public class FrankfurterClientFactoryBean implements FactoryBean<WebClient> {

    @Value("${frankfurter.base-url}")
    private String baseUrl;

    @Override
    public WebClient getObject() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        return WebClient.class;
    }
}
