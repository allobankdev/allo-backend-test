package com.allobank.allobanktest.client;

import com.allobank.allobanktest.config.FrankfurterProperties;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.web.reactive.function.client.WebClient;

public class FrankfurterClientFactory implements FactoryBean<WebClient> {

    private final FrankfurterProperties properties;

    public FrankfurterClientFactory(FrankfurterProperties properties) {
        this.properties = properties;
    }

    @Override
    public WebClient getObject() {
        return WebClient.builder()
                .baseUrl(properties.getBaseUrl())
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
