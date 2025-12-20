package com.allo_backend_test.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Component
public class FrankfurterClientFactory implements FactoryBean<WebClient>{

    private final FrankfurterProperties properties;

    public FrankfurterClientFactory(FrankfurterProperties properties) {
        this.properties = properties;
    }

    @Override
    public WebClient getObject() {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(properties.getTimeout());

        return WebClient.builder()
                .baseUrl(properties.getBaseUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        return WebClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
