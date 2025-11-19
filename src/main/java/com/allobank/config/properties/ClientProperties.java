package com.allobank.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "allo-bank.client")
public record ClientProperties(Long responseTime ,FrankFurter frankFurter, Personalization personalization) {
    public record FrankFurter(String baseUrl){}
    public record Personalization(String githubUsername){}
}
