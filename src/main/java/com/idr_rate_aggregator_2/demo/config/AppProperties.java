package com.idr_rate_aggregator_2.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private Api api = new Api();
    private Github github = new Github();

    public static class Api {
        private String baseUrl;
        private int connectionTimeout;
        private int readTimeout;

        // Getters and setters
        public String getBaseUrl() { return baseUrl; }
        public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
        public int getConnectionTimeout() { return connectionTimeout; }
        public void setConnectionTimeout(int connectionTimeout) { this.connectionTimeout = connectionTimeout; }
        public int getReadTimeout() { return readTimeout; }
        public void setReadTimeout(int readTimeout) { this.readTimeout = readTimeout; }
    }

    public static class Github {
        private String username;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
    }

    public Api getApi() { return api; }
    public void setApi(Api api) { this.api = api; }
    public Github getGithub() { return github; }
    public void setGithub(Github github) { this.github = github; }
}