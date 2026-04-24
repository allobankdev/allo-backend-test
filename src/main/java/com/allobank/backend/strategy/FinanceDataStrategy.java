package com.allobank.backend.strategy;

import com.fasterxml.jackson.databind.JsonNode;
import reactor.core.publisher.Mono;

public interface FinanceDataStrategy {
    Mono<JsonNode> fetchAndTransformData();
}