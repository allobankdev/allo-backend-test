package com.allo.app.config;

import java.time.Duration;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import com.allo.app.dto.FrankfurterProperties;

import lombok.RequiredArgsConstructor;
import reactor.netty.http.client.HttpClient;

@RequiredArgsConstructor
public class FrankfurterWebClientFactoryBean implements FactoryBean<WebClient>{

    private final FrankfurterProperties frankfurterProperties;

    @Override
    public WebClient getObject() throws Exception {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofMillis(frankfurterProperties.getTimeout()));

        return WebClient.builder()
                .baseUrl(frankfurterProperties.getUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        return WebClient.class;
    }

}
