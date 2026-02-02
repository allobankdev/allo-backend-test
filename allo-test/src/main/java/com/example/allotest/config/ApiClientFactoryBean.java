package com.example.allotest.config;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ApiClientFactoryBean implements FactoryBean<WebClient> {
    @Value("${frankfurter.api.base-url}")
    private String baseUrl;

    @Override
    public @Nullable WebClient getObject() {
        return WebClient
                .builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public @Nullable Class<?> getObjectType() {
        return WebClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
