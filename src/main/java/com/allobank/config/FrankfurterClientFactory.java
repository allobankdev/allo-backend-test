package com.allobank.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

@Component
public class FrankfurterClientFactory implements FactoryBean<RestTemplate> {

    @Value("${frankfurter.base-url}")
    private String baseUrl;

    @Value("${frankfurter.timeout:5000}")
    private int timeout;

    @Override
    public RestTemplate getObject() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(timeout);
        factory.setReadTimeout(timeout);

        RestTemplate restTemplate = new RestTemplate(factory);
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

    // Helper method untuk dapat base URL
    public String getBaseUrl() {
        return baseUrl;
    }
}