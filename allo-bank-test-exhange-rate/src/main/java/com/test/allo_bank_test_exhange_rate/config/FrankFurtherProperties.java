package com.test.allo_bank_test_exhange_rate.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.frankfurter")
public class FrankFurtherProperties {
    private String baseUrl;

    public String getBaseUrl() {
        return baseUrl;
    }
    
    public void setBaseUrl(String baseUrl){ 
        this.baseUrl = baseUrl; 
    }
}
