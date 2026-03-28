package com.allobank.allo_backend_test.finance.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class RestClientFactoryBean implements FactoryBean<RestClient> {

    private final AppConfig appConfig;

    @Override
    public RestClient getObject() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(appConfig.getDataSource().getConnectTimeout());
        factory.setReadTimeout(appConfig.getDataSource().getReadTimeout());

        return RestClient.builder()
                .baseUrl(appConfig.getDataSource().getApiUrl())
                .requestFactory(factory)
                .defaultHeader(HttpHeaders.ACCEPT, "application/json")
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        return RestClient.class;
    }
}