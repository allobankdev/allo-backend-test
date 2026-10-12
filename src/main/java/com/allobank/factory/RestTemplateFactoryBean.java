package com.allobank.factory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * Custom FactoryBean for creating and configuring the RestTemplate
 * used to communicate with external APIs.
 * 
 * This implementation demonstrates the Factory Pattern combined with Spring's
 * FactoryBean interface for centralized, reusable HTTP client configuration.
 * Benefits:
 * - Centralized configuration management
 * - Consistent timeouts and error handling across all API calls
 * - Easy to extend with additional interceptors, error handlers, etc.
 * - Lifecycle management through Spring
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RestTemplateFactoryBean implements FactoryBean<RestTemplate> {
    
    private final RestTemplateBuilder restTemplateBuilder;
    private static final int CONNECTION_TIMEOUT_MILLIS = 5000;
    private static final int READ_TIMEOUT_MILLIS = 10000;
    
    @Override
    public RestTemplate getObject() throws Exception {
        log.info("Creating configured RestTemplate instance");
        
        RestTemplate restTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofMillis(CONNECTION_TIMEOUT_MILLIS))
                .setReadTimeout(Duration.ofMillis(READ_TIMEOUT_MILLIS))
                .build();
        
        log.info("RestTemplate configured with {} ms connection timeout and {} ms read timeout",
                CONNECTION_TIMEOUT_MILLIS, READ_TIMEOUT_MILLIS);
        
        return restTemplate;
    }
    
    @Override
    public Class<?> getObjectType() {
        return RestTemplate.class;
    }
    
    @Override
    public boolean isSingleton() {
        return true;
    }
}
