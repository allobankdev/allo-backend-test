package com.finance.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "external.frankfurter")
public class FrankfurterProperties {

    private String baseUrl;

    public String getBaseUrl(){
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl){
        this.baseUrl = baseUrl;
    }
}
