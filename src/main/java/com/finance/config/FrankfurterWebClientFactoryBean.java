package com.finance.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.netty.http.client.HttpClient;
import io.netty.channel.ChannelOption;

import java.time.Duration;

@Component
public class FrankfurterWebClientFactoryBean implements FactoryBean<WebClient> {

    @Value("${frankfurter.base-url}")
    private String baseUrl;

    @Value("${frankfurter.connect-timeout-ms:3000}")
    private int connectTimeoutMs;

    @Override
    public WebClient getObject() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeoutMs)
                .responseTimeout(Duration.ofMillis(connectTimeoutMs));
        return WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public Class<?> getObjectType() { return WebClient.class; }

    @Override
    public boolean isSingleton() { return true; }
}
