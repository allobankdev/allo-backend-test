package com.allobank.idr_rate_aggregator.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Application-wide configuration properties.
 * Binds properties from application.yml with prefix "app".
 */
@Data
@Component
@ConfigurationProperties(prefix = "app")
public class ApplicationProperties {
    
    /**
     * GitHub username for personalized spread calculation.
     * This username is used to generate a unique spread factor.
     */
    private String githubUsername;
}