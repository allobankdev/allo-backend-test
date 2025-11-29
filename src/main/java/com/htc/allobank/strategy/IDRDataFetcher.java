package com.htc.allobank.strategy;

import reactor.core.publisher.Mono;

public interface IDRDataFetcher {
    Mono<Object> fetch();
}
