package com.allo.backend.test.code.service.strategy;

import com.allo.backend.test.code.model.domain.CurrenciesData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Slf4j
@Component
public class SupportedCurrenciesStrategy implements DataFetcherStrategy {

    @Override
    public String getResourceType() {
        return "supported_currencies";
    }

    @Override
    public Object fetchData(WebClient webClient) {
        log.info("Fetching supported currencies from Frankfurter API");

        Map<String, String> currencies = webClient
                .get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .block();

        if (currencies == null) {
            throw new RuntimeException("Failed to fetch supported currencies");
        }

        log.info("Fetched {} supported currencies", currencies.size());

        return CurrenciesData.builder()
                .currencies(currencies)
                .count(currencies.size())
                .build();
    }
}
