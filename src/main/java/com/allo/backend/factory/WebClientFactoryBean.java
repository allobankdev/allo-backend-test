package com.allo.backend.factory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Slf4j
@Component
public class WebClientFactoryBean implements FactoryBean<WebClient> {

    @Value("${frankfurter.api.base-url}")
    private String baseUrl;

    @Value("${frankfurter.api.timeout:10000}")
    private int timeout;

    @Override
    public WebClient getObject() throws Exception {
        log.info("Creating WebClient with base URL: {} and timeout: {}ms", baseUrl, timeout);

        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofMillis(timeout));

        return WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader("Accept", "application/json")
                .defaultHeader("Content-Type", "application/json")
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
