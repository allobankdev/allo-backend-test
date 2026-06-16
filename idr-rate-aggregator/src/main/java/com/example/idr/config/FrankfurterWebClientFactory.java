package com.example.idr.config;

import java.time.Duration;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.netty.http.client.HttpClient;

@Component
public class FrankfurterWebClientFactory
        implements FactoryBean<WebClient> {

    @Value("${frankfurter.base-url}")
    private String baseUrl;

    @Value("${frankfurter.timeout-seconds}")
    private long timeoutSeconds;

    @Override
    public WebClient getObject() {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(timeoutSeconds));

        return WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(
                        new ReactorClientHttpConnector(httpClient)
                )
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        return WebClient.class;
    }
}
