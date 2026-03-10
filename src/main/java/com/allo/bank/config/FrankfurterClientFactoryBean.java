package com.allo.bank.config;

import java.net.http.HttpClient;
import java.time.Duration;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component("frankfurterRestClient")
public class FrankfurterClientFactoryBean implements FactoryBean<RestClient> {

    private final FrankfurterProperties properties;

    public FrankfurterClientFactoryBean(FrankfurterProperties properties) {
        this.properties = properties;
    }

    @Override
    public RestClient getObject() {
        Duration connectTimeout = properties.getConnectTimeout() == null
            ? Duration.ofSeconds(3)
            : properties.getConnectTimeout();

        HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(connectTimeout)
            .build();

        return RestClient.builder()
            .baseUrl(properties.getBaseUrl())
            .requestFactory(new JdkClientHttpRequestFactoryWithTimeout(httpClient, properties.getReadTimeout()))
            .defaultHeader("Accept", "application/json")
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
