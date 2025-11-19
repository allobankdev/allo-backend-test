package com.finance.allobackend.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

public class FrankfurterClientBean implements FactoryBean<RestTemplate> {

    private final String baseUrl;
    private final Integer timeoutMs;

    public FrankfurterClientBean(String baseUrl, Integer timeoutMs) {
        this.baseUrl = baseUrl;
        this.timeoutMs = timeoutMs;
    }

    @Override
    public RestTemplate getObject() throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(timeoutMs);
        factory.setReadTimeout(timeoutMs);
        restTemplate.setRequestFactory(factory);

        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(baseUrl));

        return restTemplate;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return FactoryBean.super.isSingleton();
    }
}
