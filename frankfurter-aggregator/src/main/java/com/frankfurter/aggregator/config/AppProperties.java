package com.frankfurter.aggregator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private Api api = new Api();
    private Github github = new Github();  // CHANGED to match your YAML
    
    // Getters and Setters
    public Api getApi() { return api; }
    public void setApi(Api api) { this.api = api; }
    
    public Github getGithub() { return github; }
    public void setGithub(Github github) { this.github = github; }
    
    public String getGithubUsername() {
        return github != null ? github.getUsername() : null;
    }
    
    // Api inner class
    public static class Api {
        private String baseUrl;
        private Historical historical = new Historical();
        
        public String getBaseUrl() { return baseUrl; }
        public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
        
        public Historical getHistorical() { return historical; }
        public void setHistorical(Historical historical) { this.historical = historical; }
    }
    
    // Historical inner class
    public static class Historical {
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate startDate;
        
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate endDate;
        
        private String fromCurrency;
        private String toCurrency;
        
        // Getters and Setters
        public LocalDate getStartDate() { return startDate; }
        public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
        
        public LocalDate getEndDate() { return endDate; }
        public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
        
        public String getFromCurrency() { return fromCurrency; }
        public void setFromCurrency(String fromCurrency) { this.fromCurrency = fromCurrency; }
        
        public String getToCurrency() { return toCurrency; }
        public void setToCurrency(String toCurrency) { this.toCurrency = toCurrency; }
    }
    
    public static class Github {
        private String username;
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
    }
}