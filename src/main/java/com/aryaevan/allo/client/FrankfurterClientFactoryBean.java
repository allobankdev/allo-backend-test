package com.aryaevan.allo.client;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Factory Bean for creating and configuring WebClient instances.
 * This encapsulates the WebClient creation logic and applies base URL configuration
 * from application properties, demonstrating the FactoryBean pattern.
 */
public class FrankfurterClientFactoryBean implements FactoryBean<WebClient> {

    @Value("${external.frankfurter.base-url}")
    private String baseUrl;

    /**
     * Creates a configured WebClient instance.
     * 
     * @return Configured WebClient with base URL and default headers
     */
    @Override
    public WebClient getObject() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    /**
     * Returns the type of object created by this factory.
     * 
     * @return WebClient class
     */
    @Override
    public Class<?> getObjectType() {
        return WebClient.class;
    }

    /**
     * Indicates that this factory creates singleton instances.
     * 
     * @return true (singleton)
     */
    @Override
    public boolean isSingleton() {
        return true;
    }
}

