package com.allobank.finance.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "external.frankfurter")
public class FrankfurterProperties {

    private String baseUrl;
    private String latestIdr;
    private String historicalIdrUsd;
    private String currencies;
    
    public String getBaseUrl() {
        return baseUrl;
    }
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    public String getLatestIdr() {
        return latestIdr;
    }
    public void setLatestIdr(String latestIdr) {
        this.latestIdr = latestIdr;
    }
    public String getHistoricalIdrUsd() {
        return historicalIdrUsd;
    }
    public void setHistoricalIdrUsd(String historicalIdrUsd) {
        this.historicalIdrUsd = historicalIdrUsd;
    }
    public String getCurrencies() {
        return currencies;
    }
    public void setCurrencies(String currencies) {
        this.currencies = currencies;
    }

    
}
