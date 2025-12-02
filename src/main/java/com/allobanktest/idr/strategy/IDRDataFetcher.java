package com.allobanktest.idr.strategy;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface IDRDataFetcher {
    String key();
    Mono<Map<String, Object>> fetchData();
}
