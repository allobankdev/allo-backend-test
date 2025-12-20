package com.allo.backendtest.dto.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "frankfurter")
public record FrankfurterProperties(
        String baseUrl,
        Integer connectTimeoutMillis,
        Integer readTimeoutMillis
) {
}