package com.allobank.finnance.allobankfinance.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "external.frankfurter")
public class FrankfurterProperties {
    private String baseUrl;
    private String latestUrl;
    private String currenciesUrl;
    private int connectTimeout;
    private int readTimeout;
}
