package com.example.allotest.config;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.web.reactive.function.client.WebClient;

public class WebClientFactoryBean implements FactoryBean<WebClient> {

    private final AppProp appProp;

    public WebClientFactoryBean(AppProp appProp) {
        this.appProp = appProp;
    }

    @Override
    public WebClient getObject() throws Exception {
        return WebClient.builder().baseUrl(appProp.getExternalUrl()).build();
    }

    @Override
    public @Nullable Class<?> getObjectType() {
        return WebClient.class;
    }
    
}
