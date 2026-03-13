package com.allobank.backend.test.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "frankfurter.api")
public class FrankfurterApiProperties {

    private String baseUrl;
    private String latest;
    private String historical;
    private String currencies;
}