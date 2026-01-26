package com.chnh16.backendtest.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("config")
@Data
public class ApplicationConfig {

    private String baseCurrency;
    private String baseUrl;
    private int timeoutMs;
    private String githubUsername;

}
