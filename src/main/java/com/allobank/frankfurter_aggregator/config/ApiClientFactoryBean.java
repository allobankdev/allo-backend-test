package com.allobank.frankfurter_aggregator.config;

import java.time.Duration;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.netty.http.client.HttpClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiClientFactoryBean implements FactoryBean<WebClient> {
    
    private final AppProperties appProperties;
    
    @Override
    public WebClient getObject() {
        log.info("Creating WebClient with base URL: {}", appProperties.getFrankfurter().getBaseUrl());
        
        // PERBAIKAN DI SINI: Menggunakan HttpClient.create() yang benar
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofMillis(appProperties.getFrankfurter().getTimeout().getRead()))
                .option(io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS, 
                        appProperties.getFrankfurter().getTimeout().getConnect());
        
        return WebClient.builder()
                .baseUrl(appProperties.getFrankfurter().getBaseUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader("Accept", "application/json")
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
