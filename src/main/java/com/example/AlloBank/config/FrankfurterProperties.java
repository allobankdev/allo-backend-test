package com.example.AlloBank.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix= "frankfurter")
public class FrankfurterProperties {
    private String baseUrl;
}
