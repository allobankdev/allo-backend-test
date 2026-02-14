package com.vii.idragregator.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Luthfi Aryarizki
 * @date Created on 2026/02/14 at 09:00 p.m
 */
@Component
public class FrankfurterClientFactory implements FactoryBean<WebClient> {

    @Value("${frankfurter.api.base-url}")
    private String baseUrl;

    @Override
    public WebClient getObject() {
        return WebClient.builder().baseUrl(baseUrl).build();
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
