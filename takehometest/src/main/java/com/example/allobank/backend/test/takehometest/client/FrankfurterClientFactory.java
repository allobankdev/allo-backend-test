package com.example.allobank.backend.test.takehometest.client;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class FrankfurterClientFactory implements FactoryBean<WebClient>{

    @Value("${app.frankfurter.base.url}")
    private String baseUrl;

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
