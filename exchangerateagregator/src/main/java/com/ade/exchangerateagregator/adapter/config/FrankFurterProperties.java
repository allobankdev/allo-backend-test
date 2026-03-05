package com.ade.exchangerateagregator.adapter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Configuration
@Component
@ConfigurationProperties(prefix = "external.frankfurter")
public class FrankFurterProperties {
    private String baseUrl;
    private FrankFurterEndpoints endpoint;

    @Data
    public static class FrankFurterEndpoints {
        private String latestIdrRate;
        private String history;
        private String currency;
    }
}
