package org.imam.allo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "frankfurter.api")
public class FrankfurterProperties {
    private String baseURL;
}
