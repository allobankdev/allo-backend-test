package com.allobank.config;

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
    private int connectionTimeout = 5000;
    private int readTimeout = 10000;
    private Endpoints endpoints = new Endpoints();

    @Getter
    @Setter
    public static class Endpoints {
        private String latestIdr;
        private String historicalIdrUsd;
        private String currencies;
    }
}