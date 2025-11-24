package com.test.allo_bank_test_exhange_rate.service;

import reactor.core.publisher.Mono;

public interface IDRDataFetcher {
    Mono<Object> fetchData();
}
