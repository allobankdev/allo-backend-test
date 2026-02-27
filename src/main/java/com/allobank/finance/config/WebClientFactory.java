package com.allobank.finance.config;

import com.allobank.finance.exception.WebClientException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Slf4j
@RequiredArgsConstructor
public class WebClientFactory implements FactoryBean<WebClient> {

    private final String baseUrl;
    private final ObjectMapper objectMapper;

    @Value("${external.connect-timeout-ms}")
    private int connectTimeoutMs;

    @Value("${external.response-timeout-ms}")
    private int responseTimeoutMs;

    @Override
    public WebClient getObject() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeoutMs)
                .responseTimeout(Duration.ofMillis(responseTimeoutMs));

        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().jackson2JsonDecoder(
                        new Jackson2JsonDecoder(objectMapper)
                ))
                .codecs(configurer -> configurer.defaultCodecs().jackson2JsonEncoder(
                        new Jackson2JsonEncoder(objectMapper)
                )).build();

        return WebClient.builder()
                .baseUrl(baseUrl)
                .exchangeStrategies(exchangeStrategies)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .filter(logRequest())
                .filter(logResponse())
                .filter(globalErrorHandler())
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

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(request -> {
            log.info("WebClient Request: {} {}", request.method(), request.url());
            return Mono.just(request);
        });
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(response -> {
            log.info("WebClient Response Status: {}", response.statusCode());
            return Mono.just(response);
        });
    }

    private ExchangeFilterFunction globalErrorHandler() {
        return ExchangeFilterFunction.ofResponseProcessor(response -> {
            if (response.statusCode().is4xxClientError()) {
                return response.bodyToMono(String.class)
                        .doOnNext(body -> log.error("External API Client Error: {} {}", response.statusCode(), body))
                        .flatMap(body -> Mono.error(new WebClientException("Client Error", 400)));
            } else if (response.statusCode().is5xxServerError()) {
                return response.bodyToMono(String.class)
                        .doOnNext(body -> log.error("External API Server Error: {} {}", response.statusCode(), body))
                        .flatMap(body -> Mono.error(new WebClientException("Service Unavailable", 503)));
            }
            return Mono.just(response);
        });
    }
}