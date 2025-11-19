package com.allobank.service;

import reactor.core.publisher.Mono;

public interface IDRDataFetcher<RES> {
    Mono<RES> fetch();
}
