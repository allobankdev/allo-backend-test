package com.amri.apiintegration.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "external.frankfurter")
@Getter
@Setter
public class FrankfurterProperties {
    private String baseUrl;
    private int connectTimeoutMillis = 3000;
    private int readTimeoutMillis = 5000;
    private String userAgent = "ApiIntegration/1.0";
}
