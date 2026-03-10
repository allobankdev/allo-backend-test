package com.allo.bank.config;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "frankfurter")
public class FrankfurterProperties {

    private String baseUrl;
    private String latestPath;
    private String historicalPath;
    private String currenciesPath;
    private Duration connectTimeout;
    private Duration readTimeout;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getLatestPath() {
        return latestPath;
    }

    public void setLatestPath(String latestPath) {
        this.latestPath = latestPath;
    }

    public String getHistoricalPath() {
        return historicalPath;
    }

    public void setHistoricalPath(String historicalPath) {
        this.historicalPath = historicalPath;
    }

    public String getCurrenciesPath() {
        return currenciesPath;
    }

    public void setCurrenciesPath(String currenciesPath) {
        this.currenciesPath = currenciesPath;
    }

    public Duration getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Duration getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Duration readTimeout) {
        this.readTimeout = readTimeout;
    }
}
