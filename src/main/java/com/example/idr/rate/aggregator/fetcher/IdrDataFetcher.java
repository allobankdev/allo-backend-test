package com.example.idr.rate.aggregator.fetcher;

import reactor.core.publisher.Mono;

public interface IdrDataFetcher {
    Mono<Object> fetch();
}
