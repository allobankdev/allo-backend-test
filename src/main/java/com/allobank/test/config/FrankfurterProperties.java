package com.allobank.test.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "external.api.frankfurter")
public class FrankfurterProperties {
    private String baseUrl;
    private Endpoints endpoints;
    private int connectTimeout;

    @Data
    public static class Endpoints {
        private String latest;
        private String historical;
        private String currencies;
    }
}