package com.allobank.allobackendtest.config;

import com.allobank.allobackendtest.exception.ExternalServiceException;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientFactoryBean {

    @Value("${app.api.base-url}")
    private String baseUrl;

    @Bean
    public WebClient webClient() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 35000)
                .responseTimeout(Duration.ofSeconds(35))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(35, TimeUnit.SECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(35, TimeUnit.SECONDS)));

        return WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .filter(errorHandlerFilter())
                .build();
    }

    private ExchangeFilterFunction errorHandlerFilter() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if (clientResponse.statusCode().isError()) {
                return clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new ExternalServiceException(
                                "API Error (" + clientResponse.statusCode() + "): " + errorBody
                        )));
            }
            return Mono.just(clientResponse);
        });
    }
}
