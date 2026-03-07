package com.allo.config;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "frankfurter.api")
public record FrankfurterApiProperties(
		String baseUrl,
		Duration connectTimeout,
		Duration readTimeout) {
}
