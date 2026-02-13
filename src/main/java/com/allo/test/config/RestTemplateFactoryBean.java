package com.allo.test.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateFactoryBean implements FactoryBean<RestTemplate> {

    private final FrankfurterProperties properties;

    public RestTemplateFactoryBean(FrankfurterProperties properties) {
        this.properties = properties;
    }

    @Override
    public RestTemplate getObject() {

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(properties.getTimeout().getConnect());
        factory.setReadTimeout(properties.getTimeout().getRead());

        return new RestTemplate(factory);
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
