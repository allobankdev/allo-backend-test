package com.allobank.idr_rate_aggregator.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "frankfurter")
public class FrankfurterProperties {

    private String baseUrl;
    private Endpoints endpoints = new Endpoints();
    private Params params = new Params();

    @Data
    public static class Endpoints {
        private String latest;
        private String historical;
        private String currencies;
    }

    @Data
    public static class Params {
        private String base;
        private String from;
        private String to;
    }
}
