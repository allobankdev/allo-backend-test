package com.allo.backendtest.config;

import com.allo.backendtest.dto.FrankfurterProperties;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

public class FrankfurterRcBean implements FactoryBean<RestClient> {

    private final FrankfurterProperties properties;

    public FrankfurterRcBean(FrankfurterProperties properties) {
        this.properties = properties;
    }

    @Override
    public RestClient getObject() {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectionRequestTimeout(properties.connectTimeoutMillis());
        requestFactory.setReadTimeout(properties.readTimeoutMillis());

        BufferingClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(requestFactory);

        return RestClient.builder()
                .baseUrl(properties.baseUrl())
                .requestFactory(factory)
                .requestInterceptor(new LoggingInterceptor())
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        return RestClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}