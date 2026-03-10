package com.allobank.financeapi.service.strategy;

import com.allobank.financeapi.model.enums.ResourceType;
import reactor.core.publisher.Mono;

public interface DataFetcherStrategy {
    ResourceType getResourceType();
    Mono<Object> fetchData();
}
