package com.nurmaya.allobank.idr_rate_aggregator.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "frankfurter")
public class FrankfurterProperties {
    private String baseUrl;
    private String historicalRange;
}
