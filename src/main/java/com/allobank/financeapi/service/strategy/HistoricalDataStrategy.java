package com.allobank.financeapi.service.strategy;

import com.allobank.financeapi.model.dto.HistoricalRatesResponse;
import com.allobank.financeapi.model.enums.ResourceType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class HistoricalDataStrategy implements DataFetcherStrategy {

    private final WebClient webClient;

    public HistoricalDataStrategy(@Qualifier("frankfurterWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.HISTORICAL_IDR_USD;
    }

    @Override
    public Mono<Object> fetchData() {
        return this.webClient.get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve()
                .bodyToMono(HistoricalRatesResponse.class)
                .map(data -> (Object) data);
    }
}
