package com.example.idr.rate.aggregator.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.frankfurter")
public class FrankfurterProperties {
    private String baseUrl;
    private int connectTimeoutMs;
    private int readTimeoutMs;
}
