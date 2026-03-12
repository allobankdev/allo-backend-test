package com.musfiul.idrrateaggregator.factory;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class WebClientFactoryBean implements FactoryBean<WebClient.Builder> {

    @Override
    public WebClient.Builder getObject() {
        return WebClient.builder();
    }

    @Override
    public Class<?> getObjectType() {
        return WebClient.Builder.class;
    }

}
