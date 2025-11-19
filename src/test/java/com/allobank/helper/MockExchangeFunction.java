package com.allobank.helper;

import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

public class MockExchangeFunction implements ExchangeFunction {

    private final ClientResponse response;

    public MockExchangeFunction(ClientResponse response) {
        this.response = response;
    }

    @Override
    public Mono<ClientResponse> exchange(ClientRequest request) {
        return Mono.just(response);
    }
}