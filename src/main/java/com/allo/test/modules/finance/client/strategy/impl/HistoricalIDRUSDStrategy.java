package com.allo.test.modules.finance.client.strategy.impl;

import com.allo.test.modules.finance.client.strategy.IDRDataFetcher;
import com.allo.test.modules.finance.dto.res.HistoricalRatesResponse;
import com.allo.test.modules.finance.enums.ResourceType;
import com.allo.test.modules.finance.store.DataStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Strategy implementation for fetching historical IDR to USD exchange rates
 * for a specific date range.
 * <p>
 * Handles the "historical_idr_usd" resource type.
 */
@Slf4j
@Component("historical_idr_usd")
@RequiredArgsConstructor
public class HistoricalIDRUSDStrategy implements IDRDataFetcher {

    private static final String DATE_RANGE = "2024-01-01..2024-01-05";
    private static final String FROM_CURRENCY = "IDR";
    private static final String TO_CURRENCY = "USD";

    private final DataStore dataStore;

    @Override
    public HistoricalRatesResponse fetchData(WebClient webClient) {
        log.info("Fetching historical rates from {} to {} for date range: {}",
                FROM_CURRENCY, TO_CURRENCY, DATE_RANGE);

        HistoricalRatesResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/" + DATE_RANGE)
                        .queryParam("from", FROM_CURRENCY)
                        .queryParam("to", TO_CURRENCY)
                        .build())
                .retrieve()
                .bodyToMono(HistoricalRatesResponse.class)
                .doOnSuccess(r -> log.info("Successfully fetched historical rates for {} dates",
                        r.getRates() != null ? r.getRates().size() : 0))
                .doOnError(error -> log.error("Error fetching historical rates: {}", error.getMessage()))
                .block();

        // Store in DataStore
        if (response != null) {
            dataStore.store(getResourceType(), response);
            log.debug("Stored historical rates in DataStore");
        }

        return response;
    }

    @Override
    public Object getData() {
        return dataStore.get(getResourceType());
    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.HISTORICAL_RATES;
    }
}
