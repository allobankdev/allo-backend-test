package com.example.allobank.backend.test.takehometest.client;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.netty.http.client.HttpClient;

@Component
public class FrankfurterClientFactory implements FactoryBean<FrankfurterClient> {

    @Value("${app.frankfurter.baseurl}")
    private String baseUrl;

    @Value("${app.frankfurter.timeout-ms}")
    private int timeoutMs;

    @Override
    public FrankfurterClient getObject() {

        HttpClient httpClient = HttpClient.create()
                // connection timeout
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeoutMs)
                // response timeout
                .responseTimeout(Duration.ofMillis(timeoutMs))
                // read & write timeout
                .doOnConnected(conn -> conn.addHandlerLast(
                        new ReadTimeoutHandler(timeoutMs, TimeUnit.MILLISECONDS))
                        .addHandlerLast(
                                new WriteTimeoutHandler(timeoutMs, TimeUnit.MILLISECONDS)));

        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        return new FrankfurterClient(webClient);
    }

    @Override
    public Class<?> getObjectType() {
        return FrankfurterClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
