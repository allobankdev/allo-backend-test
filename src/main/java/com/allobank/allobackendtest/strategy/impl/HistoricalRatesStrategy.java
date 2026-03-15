package com.allobank.allobackendtest.strategy.impl;

import com.allobank.allobackendtest.exception.ExternalServiceException;
import com.allobank.allobackendtest.strategy.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("historical_idr_usd")
@RequiredArgsConstructor
public class HistoricalRatesStrategy implements IDRDataFetcher {
    private final WebClient webClient;
    @Override
    public Mono<Object> fetchData() {
        return webClient.get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve()
                .bodyToMono(Map.class)
                .onErrorMap(e -> !(e instanceof ExternalServiceException),
                        e -> new ExternalServiceException("API unavailable", e))
                .map(response -> {
                    Map<String, Map<String, Double>> rates= (Map<String, Map<String, Double>>) response.get("rates");
                    List<Map<String, Object>> resultList = new ArrayList<>();

                    if (rates != null){
                        rates.forEach((date, currencyMap) -> {
                            Map<String, Object> entry = Map.of(
                                    "date", date,
                                    "rate_USD", currencyMap.get("USD")
                            );
                            resultList.add(entry);
                        });
                    }
                    return resultList;
                });

    }

    @Override
    public boolean supports(String type) {
        return "historical_idr_usd".equals(type);
    }
}
