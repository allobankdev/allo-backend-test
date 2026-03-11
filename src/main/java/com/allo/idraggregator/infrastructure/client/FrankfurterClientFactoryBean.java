package com.allo.idraggregator.infrastructure.client;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.allo.idraggregator.infrastructure.config.properties.FrankfurterProperties;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class FrankfurterClientFactoryBean implements FactoryBean<WebClient> {

    private FrankfurterProperties properties;

    @Override
    public WebClient getObject() {

        return WebClient.builder()
                .baseUrl(properties.baseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        
        return WebClient.class;
    }
}
