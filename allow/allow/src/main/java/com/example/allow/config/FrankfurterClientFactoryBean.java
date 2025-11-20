package com.example.allow.config;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Component
public class FrankfurterClientFactoryBean implements FactoryBean<WebClient> {

    private final AppConfig config;

    public FrankfurterClientFactoryBean(AppConfig config) {
        this.config = config;
    }

    @Override
    public WebClient getObject() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .responseTimeout(Duration.ofSeconds(10));

        return WebClient.builder()
                .baseUrl(config.getBaseUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader("Accept", "application/json")
                .filter(ExchangeFilterFunction.ofRequestProcessor(request -> {
                    System.out.println("→ Request: " + request.method() + " " + request.url());
                    return Mono.just(request);
                }))
                .filter(ExchangeFilterFunction.ofResponseProcessor(response -> {
                    System.out.println("← Response: " + response.statusCode());
                    return Mono.just(response);
                }))
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