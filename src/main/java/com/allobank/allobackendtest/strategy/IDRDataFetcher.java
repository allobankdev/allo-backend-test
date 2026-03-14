package com.allobank.allobackendtest.strategy;

import reactor.core.publisher.Mono;

public interface IDRDataFetcher {
    Mono<Object> fetchData();
    boolean supports(String type);

}
