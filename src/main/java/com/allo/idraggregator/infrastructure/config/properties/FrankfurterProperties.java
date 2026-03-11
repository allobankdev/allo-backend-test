package com.allo.idraggregator.infrastructure.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "frankfurter")
public record FrankfurterProperties(
                String baseUrl,
                String historicalRange) {
}
