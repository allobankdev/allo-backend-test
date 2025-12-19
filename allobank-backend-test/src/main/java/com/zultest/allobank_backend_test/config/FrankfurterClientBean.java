package com.zultest.allobank_backend_test.config;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

public class FrankfurterClientBean implements FactoryBean<WebClient> {

    private final FrankfurterApiProperties properties;

    public FrankfurterClientBean(FrankfurterApiProperties properties) {
        this.properties = properties;
    }

    @Override
    public @Nullable WebClient getObject() throws Exception {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofMillis(properties.getTimeoutRead()));

        return WebClient.builder()
                .baseUrl(properties.getBaseUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Override
    public @Nullable Class<?> getObjectType() {
        return WebClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
