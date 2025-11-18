package com.allo.test.configs.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "frankfurter.api")
public class FrankfurterApiProperties {

    private String baseUrl;
    private Integer connectionTimeout;
    private Integer readTimeout;
    private Integer writeTimeout;
    private Integer maxInMemorySize;
}
