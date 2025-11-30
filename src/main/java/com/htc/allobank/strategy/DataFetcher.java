package com.htc.allobank.strategy;

import reactor.core.publisher.Mono;

public interface DataFetcher {
    Mono<Object> fetch();
}
