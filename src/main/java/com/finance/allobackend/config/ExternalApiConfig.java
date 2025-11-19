package com.finance.allobackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExternalApiConfig {
    @Value("${frankfurter.api.url}")
    private String baseUrl;

    @Value("${frankfurter.api.timeoutms}")
    private int timeoutMs;

    @Bean
    public FrankfurterClientBean frankfurterClient() {
        return new FrankfurterClientBean(baseUrl, timeoutMs);
    }
}
