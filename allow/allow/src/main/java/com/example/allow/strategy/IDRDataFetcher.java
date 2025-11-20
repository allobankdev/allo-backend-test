package com.example.allow.strategy;

import reactor.core.publisher.Mono;

public interface IDRDataFetcher {
    Mono<Object> fetchData();
    String getResourceKey();
}
