package com.allobank.allo_backend_test.finance.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

    private final AppConfig appConfig;

    @Bean
    public RestClient restClient() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(appConfig.getDataSource().getConnectTimeout());
        factory.setReadTimeout(appConfig.getDataSource().getReadTimeout());

        return RestClient.builder()
                .baseUrl(appConfig.getDataSource().getApiUrl())
                .requestFactory(factory)
                .defaultHeader(HttpHeaders.ACCEPT, "application/json")
                .build();
    }
}