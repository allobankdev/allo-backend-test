package com.allobank.config;

import com.allobank.config.properties.ClientProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

import static org.springframework.http.HttpHeaders.ACCEPT;

@Configuration
@RequiredArgsConstructor
public class FrankFurterClientBean implements FactoryBean<WebClient> {

    private final ClientProperties clientProperties;

    @Override
    public WebClient getObject() {
        return WebClient.builder()
                .baseUrl(clientProperties.frankFurter().baseUrl())
                .defaultHeader(ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(HttpClient
                        .create()
                        .responseTimeout(Duration.ofSeconds(clientProperties.responseTime()))))
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
