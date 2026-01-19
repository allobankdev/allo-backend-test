package com.example.allo.client.impl;

import com.example.allo.client.FrankfurterClient;
import com.example.allo.properties.FrankfurterProperties;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class FrankfurterClientFactoryBean implements FactoryBean<FrankfurterClient> {
    private final FrankfurterProperties properties;

    public FrankfurterClientFactoryBean(FrankfurterProperties properties) {
        this.properties = properties;
    }

    @Override
    public FrankfurterClient getObject() {
        WebClient client = WebClient.builder()
                .baseUrl(properties.getBaseUrl())
                .build();
        return new FrankfurterClientImpl(client);
    }

    @Override
    public Class<?> getObjectType() {
        return FrankfurterClient.class;
    }
}
