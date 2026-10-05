package com.finance.aggregator.strategy;

import reactor.core.publisher.Mono;

public interface DataFetcherStrategy {
    Mono<Object> fetch();
    String getType();
}