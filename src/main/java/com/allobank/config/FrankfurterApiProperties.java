package com.allobank.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Data
@Configuration
@ConfigurationProperties(prefix = "frankfurter.api")
public class FrankfurterApiProperties {
    private String baseUrl;
    private Duration connectTimeout = Duration.ofSeconds(5);
    private Duration readTimeout = Duration.ofSeconds(10);
    private Historical historical = new Historical();

    @Data
    public static class Historical {
        private String startDate;
        private String endDate;
        private String fromCurrency;
        private String toCurrency;
    }
}

