package com.allobank.strategy.impl;

import com.allobank.config.FrankfurterApiProperties;
import com.allobank.dto.HistoricalRatesResponse;
import com.allobank.strategy.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class HistoricalIdrUsdStrategy implements IDRDataFetcher {

    private static final String RESOURCE_TYPE = "historical_idr_usd";

    private final WebClient webClient;
    private final FrankfurterApiProperties apiProperties;

    @Override
    public Mono<Object> fetchData() {
        log.info("Fetching historical IDR to USD rates from Frankfurter API");
        
        var historical = apiProperties.getHistorical();
        String dateRange = String.format("%s..%s", historical.getStartDate(), historical.getEndDate());
        
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/{dateRange}")
                        .queryParam("from", historical.getFromCurrency())
                        .queryParam("to", historical.getToCurrency())
                        .build(dateRange))
                .retrieve()
                .bodyToMono(HistoricalRatesResponse.class)
                .cast(Object.class)
                .doOnError(error -> log.error("Error fetching historical IDR to USD rates", error));
    }

    @Override
    public String getResourceType() {
        return RESOURCE_TYPE;
    }
}

