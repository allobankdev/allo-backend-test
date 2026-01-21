package com.allo.app.service;

import reactor.core.publisher.Mono;

public interface IDRDataFetcher<T> {
    Mono<T> getData(); 
}
