package com.allo.backendtest.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "frankfurter")
public record FrankfurterProperties(
    String baseUrl,
    int connectTimeoutMillis,
    int readTimeoutMillis
) {}