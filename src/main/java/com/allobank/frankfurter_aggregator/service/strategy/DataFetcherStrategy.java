package com.allobank.frankfurter_aggregator.service.strategy;

import reactor.core.publisher.Mono;

public interface DataFetcherStrategy {
    String getResourceType();
    Mono<Object> fetchData();
}
