package com.allobank.idr_rate_aggregator.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for Frankfurter API integration.
 * Externalizes API URLs, timeouts, and other settings.
 */
@Data
@Component
@ConfigurationProperties(prefix = "frankfurter.api")
public class FrankfurterApiProperties {
    
    /**
     * Base URL of the Frankfurter API.
     */
    private String baseUrl;
    
    /**
     * Timeout configuration for API calls.
     */
    private Timeout timeout = new Timeout();
    
    /**
     * Historical data configuration.
     */
    private Historical historical = new Historical();
    
    @Data
    public static class Timeout {
        /**
         * Connection timeout in milliseconds.
         */
        private int connect = 5000;
        
        /**
         * Read timeout in milliseconds.
         */
        private int read = 10000;
    }
    
    @Data
    public static class Historical {
        /**
         * Start date for historical data (YYYY-MM-DD).
         */
        private String startDate;
        
        /**
         * End date for historical data (YYYY-MM-DD).
         */
        private String endDate;
    }
}