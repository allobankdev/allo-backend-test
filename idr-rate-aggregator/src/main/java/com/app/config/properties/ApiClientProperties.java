package com.app.config.properties;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
public class ApiClientProperties {

    @Value("${api.client.baseurl}")
    private String baseUrl;

    @Value("${api.client.connectiontimeout}")
    private int connectionTimeout;

    @Value("${api.client.readtimeout}")
    private int readTimeout;

    // Getters and setters
    public String getBaseUrl() {
        return baseUrl;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

}