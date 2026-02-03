package com.example.allobank.client.factory;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class FrankfurterClientFactory
        implements FactoryBean<RestTemplate> {

    @Value("${frankfurter.base-url}")
    private String baseUrl;

    @Override
    public RestTemplate getObject() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

    @Override
    public Class<?> getObjectType() {
        return RestTemplate.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}

