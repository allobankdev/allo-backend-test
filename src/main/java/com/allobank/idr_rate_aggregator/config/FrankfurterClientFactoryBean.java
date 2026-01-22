package com.allobank.idr_rate_aggregator.config;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

/**
 * Factory bean for creating WebClient instance for Frankfurter API.
 * Implements FactoryBean to customize WebClient creation and configuration.
 * 
 * This approach allows for:
 * - Centralized client configuration
 * - Externalized properties (base URL, timeouts)
 * - Reusable client instance across strategies
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FrankfurterClientFactoryBean implements FactoryBean<WebClient> {

    private final FrankfurterApiProperties properties;

    @Override
    public WebClient getObject() {
        log.info("Creating WebClient for Frankfurter API with base URL: {}", 
                properties.getBaseUrl());
        
        return WebClient.builder()
                .baseUrl(properties.getBaseUrl())
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024)) // 16MB buffer
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        return WebClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true; // Single shared instance
    }
}

