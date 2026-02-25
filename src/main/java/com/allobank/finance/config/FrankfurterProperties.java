package com.allobank.finance.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

// Todo : frankfurter properties
@Data
@Component
@ConfigurationProperties(prefix = "frankfurter")
public class FrankfurterProperties {

    private String baseUrl;

    private String githubUsername;

    private String historicalStart;

    private String historicalEnd;

    private int timeoutSeconds = 10;
}
