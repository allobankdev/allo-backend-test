package com.sdewa.IdrRateAggregator.webclients;

import java.time.Duration;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.netty.http.client.HttpClient;

@Component
public class FrankfurterWebClientFactory implements FactoryBean<WebClient> {

    @Value("${frankfurter.base-url}")
    private String baseUrl;

    @Override
    public WebClient getObject() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create().responseTimeout(Duration.ofSeconds(5))))
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        return WebClient.class;
    }
}
