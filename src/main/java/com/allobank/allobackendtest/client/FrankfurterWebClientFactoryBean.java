package com.allobank.allobackendtest.client;

import java.time.Duration;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import com.allobank.allobackendtest.config.FrankfurterProperties;

import reactor.netty.http.client.HttpClient;

public class FrankfurterWebClientFactoryBean implements FactoryBean<WebClient> {

    private final FrankfurterProperties properties;

    public FrankfurterWebClientFactoryBean(FrankfurterProperties properties) {
        this.properties = properties;
    }

    @Override
    public @Nullable WebClient getObject() throws Exception {

        HttpClient httpClient = HttpClient.create().responseTimeout(Duration.ofMillis(properties.getTimeoutMs()));

        WebClient webClient = WebClient.builder().baseUrl(properties.getBaseUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient)).build();

       return webClient;
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
