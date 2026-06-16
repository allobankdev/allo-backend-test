package com.allobank.strategy.impl;

import com.allobank.dto.CurrenciesResponse;
import com.allobank.strategy.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class SupportedCurrenciesStrategy implements IDRDataFetcher {

    private static final String RESOURCE_TYPE = "supported_currencies";

    private final WebClient webClient;

    @Override
    public Mono<Object> fetchData() {
        log.info("Fetching supported currencies from Frankfurter API");
        
        return webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(CurrenciesResponse.class)
                .cast(Object.class)
                .doOnError(error -> log.error("Error fetching supported currencies", error));
    }

    @Override
    public String getResourceType() {
        return RESOURCE_TYPE;
    }
}

