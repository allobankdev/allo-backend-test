package com.allo.test.modules.finance.client.strategy.impl;

import com.allo.test.modules.finance.dto.res.CurrenciesResponse;
import com.allo.test.modules.finance.client.strategy.FrankfurterResourceStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Slf4j
@Component
public class CurrenciesStrategy implements FrankfurterResourceStrategy<CurrenciesResponse> {

    private static final String ENDPOINT = "/currencies";

    @Override
    public CurrenciesResponse fetchData(WebClient webClient) {
        log.info("Fetching list of supported currencies");

        Map<String, String> currencies = webClient.get()
                .uri(ENDPOINT)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .doOnSuccess(response -> log.info("Successfully fetched {} currencies",
                        response != null ? response.size() : 0))
                .doOnError(error -> log.error("Error fetching currencies: {}", error.getMessage()))
                .block();

        return CurrenciesResponse.builder()
                .currencies(currencies)
                .build();
    }

    @Override
    public String getStrategyName() {
        return "CurrenciesStrategy";
    }
}
