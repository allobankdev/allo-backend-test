package com.allobankdev.allo_idr_rate_aggregator.strategy;

import java.util.List;

import reactor.core.publisher.Mono;

public interface IDRDataFetcher {
    String resourceType();
    // Mono<List<Object>>;
    Mono<List<Object>> fetch();
}

