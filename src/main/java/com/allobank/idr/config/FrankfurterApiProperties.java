package com.allobank.idr.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "frankfurter.api")
@Getter
@Setter
public class FrankfurterApiProperties {
    private String baseUrl;
    private int timeout;
}
