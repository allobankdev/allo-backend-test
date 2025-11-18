package com.allo.test.modules.finance.client.strategy.impl;

import com.allo.test.modules.finance.dto.res.HistoricalRatesResponse;
import com.allo.test.modules.finance.client.strategy.FrankfurterResourceStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
public class HistoricalRatesStrategy implements FrankfurterResourceStrategy<HistoricalRatesResponse> {

    private static final String DATE_RANGE = "2024-01-01..2024-01-05";
    private static final String FROM_CURRENCY = "IDR";
    private static final String TO_CURRENCY = "USD";

    @Override
    public HistoricalRatesResponse fetchData(WebClient webClient) {
        log.info("Fetching historical rates from {} to {} for date range: {}",
                FROM_CURRENCY, TO_CURRENCY, DATE_RANGE);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/" + DATE_RANGE)
                        .queryParam("from", FROM_CURRENCY)
                        .queryParam("to", TO_CURRENCY)
                        .build())
                .retrieve()
                .bodyToMono(HistoricalRatesResponse.class)
                .doOnSuccess(response -> log.info("Successfully fetched historical rates for {} dates",
                        response.getRates() != null ? response.getRates().size() : 0))
                .doOnError(error -> log.error("Error fetching historical rates: {}", error.getMessage()))
                .block();
    }

    @Override
    public String getStrategyName() {
        return "HistoricalRatesStrategy";
    }
}
