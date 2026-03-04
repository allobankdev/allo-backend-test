package com.finance.factory;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.finance.config.FrankfurterProperties;

@Component
public class WebClientFactoryBean implements FactoryBean<WebClient>{

    private final FrankfurterProperties properties;

    public WebClientFactoryBean(FrankfurterProperties properties){
        this.properties = properties;
    }

    @Override
    public WebClient getObject() {

        return WebClient.builder()
            .baseUrl(properties.getBaseUrl())
            .defaultHeader("Content-Type", "application/json")
            .build();
    }

    @Override
    public Class<?> getObjectType() {
        return WebClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
