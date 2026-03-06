package com.springboot.test.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "api.currency")
public class CurrencyApiConfig {

    private String latestRateUrl;
    private String historicalUrl;
    private String supportedCurrenciesUrl;

    public String getLatestRateUrl() {
        return latestRateUrl;
    }

    public void setLatestRateUrl(String latestRateUrl) {
        this.latestRateUrl = latestRateUrl;
    }

    public String getHistoricalUrl() {
        return historicalUrl;
    }

    public void setHistoricalUrl(String historicalUrl) {
        this.historicalUrl = historicalUrl;
    }

    public String getSupportedCurrenciesUrl() {
        return supportedCurrenciesUrl;
    }

    public void setSupportedCurrenciesUrl(String supportedCurrenciesUrl) {
        this.supportedCurrenciesUrl = supportedCurrenciesUrl;
    }
}
