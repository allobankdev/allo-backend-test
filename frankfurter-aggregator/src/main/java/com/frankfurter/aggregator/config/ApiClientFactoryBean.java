package com.frankfurter.aggregator.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Component
public class ApiClientFactoryBean implements FactoryBean<RestTemplate> {
    
    private final AppProperties appProperties;
    
    public ApiClientFactoryBean(AppProperties appProperties) {
        this.appProperties = appProperties;
    }
    
    @Override
    public RestTemplate getObject() {
        // Create and configure RestTemplate
        RestTemplate restTemplate = new RestTemplate();
        
        String baseUrl = appProperties.getApi().getBaseUrl();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(baseUrl));
        
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);    // 5 seconds connection timeout
        factory.setReadTimeout(10000);      // 10 seconds read timeout
        restTemplate.setRequestFactory(factory);
        
       
        
        System.out.println("RestTemplate configured with base URL: " + baseUrl);
        
        return restTemplate;
    }
    
    @Override
    public Class<?> getObjectType() {
        return RestTemplate.class;
    }
    
    @Override
    public boolean isSingleton() {
        return true;  
    }
}