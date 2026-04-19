package com.allobankdev.exchangrate.config;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.web.client.RestTemplate;

public class RestTemplateFactory implements FactoryBean<RestTemplate> {

    @Override
    public @Nullable RestTemplate getObject() throws Exception {
        return new RestTemplate();
    }

    @Override
    public @Nullable Class<?> getObjectType() {
        return RestTemplate.class;
    }

    @Override
    public boolean isSingleton() {
        return FactoryBean.super.isSingleton();
    }
}
