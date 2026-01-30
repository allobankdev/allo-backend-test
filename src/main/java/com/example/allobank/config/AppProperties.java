package com.example.allobank.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "frankfurter")
@Getter
@Setter
public class AppProperties {
    private String baseUrl;
}