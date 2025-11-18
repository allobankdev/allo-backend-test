package com.allo.test.modules.finance.client.strategy.impl;

import com.allo.test.modules.finance.client.strategy.IDRDataFetcher;
import com.allo.test.modules.finance.dto.res.CurrenciesResponse;
import com.allo.test.modules.finance.enums.ResourceType;
import com.allo.test.modules.finance.store.DataStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

/**
 * Strategy implementation for fetching the list of all supported currency symbols
 * from the Frankfurter API.
 * <p>
 * Handles the "supported_currencies" resource type.
 */
@Slf4j
@Component("supported_currencies")
@RequiredArgsConstructor
public class SupportedCurrenciesStrategy implements IDRDataFetcher {

    private static final String ENDPOINT = "/currencies";

    private final DataStore dataStore;

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

        CurrenciesResponse response = CurrenciesResponse.builder()
                .currencies(currencies)
                .build();

        // Store in DataStore
        if (response != null) {
            dataStore.store(getResourceType(), response);
            log.debug("Stored currencies in DataStore");
        }

        return response;
    }

    @Override
    public Object getData() {
        return dataStore.get(getResourceType());
    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.CURRENCIES;
    }
}
