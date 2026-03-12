package com.allobank.financeapi.service.strategy;

import com.allobank.financeapi.model.FinanceData;
import reactor.core.publisher.Mono;

public interface IDRDataFetcher {
    boolean supports(String resourceType);
    Mono<FinanceData> fetchData(); // fetch from Frankfurter API
    String getResourceType();
}