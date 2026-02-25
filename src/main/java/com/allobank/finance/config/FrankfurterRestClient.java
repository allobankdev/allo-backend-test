package com.allobank.finance.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class FrankfurterRestClient implements FactoryBean<RestClient> {

    private final FinanceApiProperties properties;

    @Override
    public RestClient getObject() {
        FinanceApiProperties.Api apiConfig = properties.getApi();

        log.info("Creating RestClient with base URL: {}, connect timeout: {}ms, read timeout: {}ms",
                apiConfig.getBaseUrl(),
                apiConfig.getConnectTimeout(),
                apiConfig.getReadTimeout());

        ClientHttpRequestFactory requestFactory = ClientHttpRequestFactoryBuilder.simple()
                .withCustomizer(simpleClientHttpRequestFactory -> {
                    simpleClientHttpRequestFactory.setReadTimeout(Duration.ofMillis(properties.getApi().getReadTimeout()));
                    simpleClientHttpRequestFactory.setConnectTimeout(Duration.ofMillis(properties.getApi().getConnectTimeout()));
                }).build();

        return RestClient.builder()
                .baseUrl(apiConfig.getBaseUrl())
                .requestFactory(requestFactory)
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        return RestClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}