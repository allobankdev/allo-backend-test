package com.allobankdev.allo_idr_rate_aggregator.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "frankfurter")
@Data
public class FrankfurterProperties {

    private String baseUrl;
    private int timeoutMs;

}
