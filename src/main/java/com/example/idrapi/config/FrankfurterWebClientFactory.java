package com.example.idrapi.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Constraint B: FactoryBean implementation for WebClient.
 *
 * Using FactoryBean<WebClient> instead of a plain @Bean method gives us:
 *  - Full Spring lifecycle integration (afterPropertiesSet, isSingleton, etc.)
 *  - A dedicated, self-contained class that encapsulates ALL client construction
 *    logic, keeping @Configuration classes clean and focused on wiring.
 *  - The ability to validate required properties before the bean is returned,
 *    failing fast at startup rather than at first use.
 */
@Component("frankfurterWebClientFactory")
public class FrankfurterWebClientFactory implements FactoryBean<WebClient> {

    private static final Logger log = LoggerFactory.getLogger(FrankfurterWebClientFactory.class);

    private static final int CONNECT_TIMEOUT_MS = 5_000;
    private static final int READ_TIMEOUT_SEC   = 10;
    private static final int WRITE_TIMEOUT_SEC  = 10;

    private final FrankfurterProperties properties;

    public FrankfurterWebClientFactory(FrankfurterProperties properties) {
        this.properties = properties;
    }

    /**
     * Builds and returns a fully-configured, singleton WebClient instance.
     * Called once by the Spring container.
     */
    @Override
    public WebClient getObject() {
        log.info("Building Frankfurter WebClient with base URL: {}", properties.getBaseUrl());

        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIMEOUT_MS)
                .responseTimeout(Duration.ofSeconds(READ_TIMEOUT_SEC))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(READ_TIMEOUT_SEC, TimeUnit.SECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(WRITE_TIMEOUT_SEC, TimeUnit.SECONDS)));

        return WebClient.builder()
                .baseUrl(properties.getBaseUrl())
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .filter(logRequest())
                .filter(logResponse())
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        return WebClient.class;
    }

    /** Singleton: only one WebClient instance is created per application context. */
    @Override
    public boolean isSingleton() {
        return true;
    }

    // ------------------------------------------------------------------ filters

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(request -> {
            log.debug("HTTP Request: {} {}", request.method(), request.url());
            return Mono.just(request);
        });
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(response -> {
            log.debug("HTTP Response status: {}", response.statusCode());
            return Mono.just(response);
        });
    }
}
