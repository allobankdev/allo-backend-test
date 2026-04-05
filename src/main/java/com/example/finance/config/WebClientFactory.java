package com.example.finance.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class WebClientFactory implements FactoryBean<WebClient> {

    @Value("${api.frankfurter.base-url}")
    private String baseUrl;

    @Override
    public WebClient getObject() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .filter((request, next) -> {
                    System.out.println("REQUEST URL: " + request.url());
                    return next.exchange(request);
                })
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        return WebClient.class;
    }
}