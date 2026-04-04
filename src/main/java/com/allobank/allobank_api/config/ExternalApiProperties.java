package com.allobank.allobank_api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "external.api")
public class ExternalApiProperties {

    private ApiConfig frankfurter;

    public static class ApiConfig {
    
        private String baseUrl;
    
        public String getBaseUrl() { 
            return baseUrl; 
        }
        public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
        
    }

    public ApiConfig getFrankfurter() { 
        return frankfurter; 
    }

    public void setFrankfurter(ApiConfig frankfurter) { 
        this.frankfurter = frankfurter; 
    }
    
}
