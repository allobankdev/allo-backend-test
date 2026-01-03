package com.allobank.allobackendtest.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "frankfurter")
public class FrankfurterProperties {

    private String baseUrl;
    private int timeoutMs;

    public String getBaseUrl() {
        return baseUrl;
    }
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    public int getTimeoutMs() {
        return timeoutMs;
    }
    public void setTimeoutMs(int timeoutMs) {
        this.timeoutMs = timeoutMs;
    }

}
