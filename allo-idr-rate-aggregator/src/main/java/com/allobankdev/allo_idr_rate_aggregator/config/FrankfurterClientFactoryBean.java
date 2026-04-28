package com.allobankdev.allo_idr_rate_aggregator.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.Data;

@Component
@Data
public class FrankfurterClientFactoryBean implements FactoryBean<WebClient> {

    private final FrankfurterProperties frankfurterProperties;

    public FrankfurterClientFactoryBean(FrankfurterProperties frankfurterProperties) {
        this.frankfurterProperties = frankfurterProperties;
    }

    @Override
    public WebClient getObject() {
        return WebClient.builder()
                .baseUrl(frankfurterProperties.getBaseUrl())
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

