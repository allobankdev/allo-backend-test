package com.example.AlloBank.client;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.web.client.RestTemplate;

public class FrankfurterRestTemplateFactoryBean implements FactoryBean<RestTemplate> {

    private final String baseUrl;

    public FrankfurterRestTemplateFactoryBean(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public RestTemplate getObject() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(
                new org.springframework.web.util.DefaultUriBuilderFactory(baseUrl)
        );
        return restTemplate;
    }

    @Override
    public Class<?> getObjectType() {

        return RestTemplate.class;
    }
}
