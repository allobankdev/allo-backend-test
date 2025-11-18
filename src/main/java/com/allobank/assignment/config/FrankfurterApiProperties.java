package com.allobank.assignment.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

import java.time.Duration;

@ConfigurationProperties(prefix = "frankfurter")
public class FrankfurterApiProperties {
    private String baseUrl;
    private String githubUsername;
    private Duration connectionTimeout = Duration.ofSeconds(5);
    private Duration readTimeout = Duration.ofSeconds(5);
    private final HistoricalProperties historical = new HistoricalProperties();

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getGithubUsername() {
        return githubUsername;
    }

    public void setGithubUsername(String githubUsername) {
        this.githubUsername = githubUsername;
    }

    public Duration getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(Duration connectionTimeout) {
        Assert.notNull(connectionTimeout, "Connection timeout must not be null");
        this.connectionTimeout = connectionTimeout;
    }

    public Duration getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Duration readTimeout) {
        Assert.notNull(readTimeout, "Read timeout must not be null");
        this.readTimeout = readTimeout;
    }

    public HistoricalProperties getHistorical() {
        return historical;
    }
}
