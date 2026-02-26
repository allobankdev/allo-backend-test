package com.api.allorestapi.strategy;

import com.api.allorestapi.model.FinanceDataResponse;
import reactor.core.publisher.Mono;

public interface IDRDataFetch {
    String getResourceType();
    Mono<FinanceDataResponse> fetch();
}
