package com.allobank.financeapi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.time.Duration;

@Data
@Component
@ConfigurationProperties(prefix = "app.frankfurter")
public class FrankfurterApiProperties {
    private String baseUrl;
    private Duration connectTimeout = Duration.ofSeconds(5);
    private Duration readTimeout = Duration.ofSeconds(10);
    private String githubUsername; // for spread factor calculation
}