package com.allo.test.modules.finance.client.strategy.impl;

import com.allo.test.modules.finance.dto.res.LatestRatesResponse;
import com.allo.test.modules.finance.client.strategy.FrankfurterResourceStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
public class LatestRatesStrategy implements FrankfurterResourceStrategy<LatestRatesResponse> {

    private static final String ENDPOINT = "/latest";
    private static final String BASE_CURRENCY = "IDR";

    @Override
    public LatestRatesResponse fetchData(WebClient webClient) {
        log.info("Fetching latest rates with base currency: {}", BASE_CURRENCY);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(ENDPOINT)
                        .queryParam("base", BASE_CURRENCY)
                        .build())
                .retrieve()
                .bodyToMono(LatestRatesResponse.class)
                .doOnSuccess(response -> log.info("Successfully fetched latest rates for {} currencies",
                        response.getRates() != null ? response.getRates().size() : 0))
                .doOnError(error -> log.error("Error fetching latest rates: {}", error.getMessage()))
                .block();
    }

    @Override
    public String getStrategyName() {
        return "LatestRatesStrategy";
    }
}
