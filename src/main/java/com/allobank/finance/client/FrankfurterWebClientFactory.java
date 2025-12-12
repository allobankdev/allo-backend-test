package com.allobank.finance.client;

import java.time.Duration;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import com.allobank.finance.config.FrankfurterClientProperties;

import io.netty.channel.ChannelOption;
import reactor.netty.http.client.HttpClient;

public class FrankfurterWebClientFactory implements FactoryBean<WebClient> {

    private final FrankfurterClientProperties properties;

    public FrankfurterWebClientFactory(FrankfurterClientProperties properties) {
        this.properties = properties;
    }

    @Override
    public WebClient getObject() {
        return WebClient.builder()
            .baseUrl(properties.getBaseUrl())
            .clientConnector(new ReactorClientHttpConnector(
                HttpClient.create()
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, properties.getConnectTimeoutMs())
                    .responseTimeout(Duration.ofMillis(properties.getReadTimeoutMs()))))
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
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
