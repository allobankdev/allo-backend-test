package com.api.allorestapi.client;

import reactor.core.publisher.Mono;

public interface Frankfurter<T> {
    Mono<T> fetch();
}
