package com.allobank.finance.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateFactoryBean implements FactoryBean<RestTemplate> {

    private final FrankfurterApiProperties properties;

    public RestTemplateFactoryBean(FrankfurterApiProperties properties) {
        this.properties = properties;
    }

    @Override
    public RestTemplate getObject() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(properties.getConnectTimeoutMillis());
        requestFactory.setReadTimeout(properties.getReadTimeoutMillis());
        return new RestTemplate(requestFactory);
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
