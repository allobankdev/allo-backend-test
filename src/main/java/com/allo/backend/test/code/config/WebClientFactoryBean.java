package com.allo.backend.test.code.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Slf4j
@Component
public class WebClientFactoryBean implements FactoryBean<WebClient> {

    private final String baseUrl;
    private final long timeout;

    public WebClientFactoryBean(
            @Value("${frankfurter.api.base-url}") String baseUrl,
            @Value("${frankfurter.api.timeout}") long timeout) {
        this.baseUrl = baseUrl;
        this.timeout = timeout;
        log.info("WebClientFactoryBean initialized with baseUrl: {} and timeout: {}ms", baseUrl, timeout);
    }

    @Override
    public WebClient getObject() {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofMillis(timeout));

        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
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
