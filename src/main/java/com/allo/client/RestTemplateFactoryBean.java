package com.allo.client;

import java.time.Duration;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.allo.config.FrankfurterApiProperties;

@Component
public class RestTemplateFactoryBean implements FactoryBean<RestTemplate> {

    private final FrankfurterApiProperties properties;

    public RestTemplateFactoryBean(FrankfurterApiProperties properties) {
        this.properties = properties;
    }

    @Override
    public RestTemplate getObject() {
        Duration connectTimeout = properties.connectTimeout() != null
                ? properties.connectTimeout()
                : Duration.ofSeconds(5);
        Duration readTimeout = properties.readTimeout() != null
                ? properties.readTimeout()
                : Duration.ofSeconds(10);

        return new RestTemplateBuilder()
                .rootUri(properties.baseUrl())
                .setConnectTimeout(connectTimeout)
                .setReadTimeout(readTimeout)
                .build();
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
