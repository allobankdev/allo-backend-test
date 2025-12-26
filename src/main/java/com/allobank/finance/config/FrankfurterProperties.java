package com.allobank.finance.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;


@ConfigurationProperties(prefix = "frankfurter")
@Getter 
@Setter
public class FrankfurterProperties {
    private String baseUrl;
    private int timeoutMs;
}
