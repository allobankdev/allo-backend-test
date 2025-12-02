package com.allobanktest.idr.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class WebClientFactoryBean implements FactoryBean<WebClient> {

    @Value("${app.external.frankfurter-base-url}")
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

    @Override
    public boolean isSingleton() {
        return true;
    }
}
