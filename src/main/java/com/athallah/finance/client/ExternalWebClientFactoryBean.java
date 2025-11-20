package com.athallah.finance.client;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Component
public class ExternalWebClientFactoryBean implements FactoryBean<WebClient> {

    @Value("${finance.frankfurter.base-url}")
    private String baseUrl;

    @Value("${finance.frankfurter.timeout.connection:5000}")
    private int connectionTimeout;

    @Value("${finance.frankfurter.timeout.response:10000}")
    private int responseTimeout;

    @Override
    public WebClient getObject() {
        if (baseUrl == null || baseUrl.isBlank()) {
            throw new IllegalStateException("finance.frankfurter.base-url must be configured");
        }

        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofMillis(responseTimeout))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeout);

        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
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