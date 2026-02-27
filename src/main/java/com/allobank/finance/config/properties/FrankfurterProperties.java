package com.allobank.finance.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "frankfurter")
public record FrankfurterProperties(
        String baseUrl,
        int connectTimeoutMs,
        int responseTimeoutMs,
        Retry retry,
        Resources resources

) {
    public record Retry(int count, long delayMs) {
    }

    public record Resources(
            Latest latest,
            Historical historical
    ) {
        public record Latest(String base, String currency) {
        }

        public record Historical(String range, String from, String to) {
        }
    }
}
