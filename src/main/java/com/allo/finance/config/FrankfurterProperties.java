package com.allo.finance.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "frankfurter")
public record FrankfurterProperties(String baseUrl) {
}
