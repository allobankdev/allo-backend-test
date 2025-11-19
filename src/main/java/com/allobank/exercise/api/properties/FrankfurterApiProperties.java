package com.allobank.exercise.api.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FrankfurterApiProperties {

    @Value("${integration.api.frankfurter.path.currencies}")
    private String currencyPath;
    @Value("${integration.api.frankfurter.path.lates-idr}")
    private String latestIdrPath;
    @Value("${integration.api.frankfurter.base-url}")
    private String baseUrl;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getCurrencyPath() {
        return currencyPath;
    }

    public void setCurrencyPath(String currencyPath) {
        this.currencyPath = currencyPath;
    }

    public String getLatestIdrPath() {
        return latestIdrPath;
    }

    public void setLatestIdrPath(String latestIdrPath) {
        this.latestIdrPath = latestIdrPath;
    }
}
