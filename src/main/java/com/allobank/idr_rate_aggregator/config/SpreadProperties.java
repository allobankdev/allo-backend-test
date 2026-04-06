package com.allobank.idr_rate_aggregator.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.spread")
public class SpreadProperties {

    private String githubUsername;
    private double factor;
}
