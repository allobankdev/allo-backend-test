package com.example.financedata.fetcher;

import reactor.core.publisher.Mono;

public interface IDRDataFetcher {
    /**
     * fetch the data from frankfurter and return a POJO or DTO JSON-serializable object.
     * Using Mono to keep implementations testable (can be block() in runner).
     */
    Mono<Object> fetch();
    /**
     * Resource type key string mapped in the controller and registry
     */
    String resourceKey();
}