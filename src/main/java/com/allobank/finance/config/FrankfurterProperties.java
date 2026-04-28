package com.allobank.finance.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "frankfurter")
public class FrankfurterProperties {

    private String baseUrl;
    private Historical historical = new Historical();
    private int connectTimeoutMs = 5000;
    private int readTimeoutMs = 10000;

    @Getter
    @Setter
    public static class Historical {
        private String from = "2024-01-01";
        private String to = "2024-01-05";
    }
}