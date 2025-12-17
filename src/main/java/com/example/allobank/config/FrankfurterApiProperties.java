package com.example.allobank.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "frankfurter.api")
public class FrankfurterApiProperties {
    /**
     * Example: https://api.frankfurter.app
     */
    private String baseUrl;

    /**
     * milliseconds
     */
    private int connectTimeout = 5000;

    /**
     * milliseconds
     */
    private int readTimeout = 5000;
}