package com.allobank.allobackend.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class FrankfurterClientFactory implements FactoryBean<RestClient> {

    @Value("${api.base-url}")
    private String baseUrl;

    @Override
    public RestClient getObject(){
        return RestClient.builder().baseUrl(baseUrl).build();
    }

    public Class<?> getObjectType(){
        return RestClient.class;
    }
}
