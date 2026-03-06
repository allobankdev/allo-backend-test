package com.springboot.test.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class RestTemplateFactoryBean implements FactoryBean<RestTemplate> {

    @Override
    public RestTemplate getObject() throws Exception {
        ClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
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
