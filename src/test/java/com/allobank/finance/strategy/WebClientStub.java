package com.allobank.finance.strategy;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;

final class WebClientStub {

    private WebClientStub() {
    }

    static WebClient jsonClient(String json, AtomicReference<String> capturedUrl) {
        ExchangeFunction exchangeFunction = request -> {
            capturedUrl.set(request.url().toString());
            return Mono.just(ClientResponse.create(HttpStatus.OK)
                    .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .body(json)
                    .build());
        };

        return WebClient.builder()
                .baseUrl("https://api.frankfurter.app")
                .exchangeFunction(exchangeFunction)
                .build();
    }
}
