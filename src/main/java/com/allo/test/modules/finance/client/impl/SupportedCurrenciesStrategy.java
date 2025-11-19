package com.allo.test.modules.finance.client.impl;

import com.allo.test.configs.properties.FrankfurterApiProperties;
import com.allo.test.modules.finance.client.IDRDataFetcher;
import com.allo.test.modules.finance.dto.res.CurrenciesResponse;
import com.allo.test.modules.finance.enums.ResourceType;
import com.allo.test.modules.finance.service.DataStoreService;
import com.allo.test.shared.utils.WebClientErrorHandler;
import io.github.resilience4j.retry.annotation.Retry;
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
@Component
@RequiredArgsConstructor
public class SupportedCurrenciesStrategy implements IDRDataFetcher {

    private final FrankfurterApiProperties apiProperties;
    private final DataStoreService dataStoreService;

    @Override
    @Retry(name = "frankfurterApi")
    public CurrenciesResponse fetchData(WebClient webClient) {
        String endpoint = apiProperties.getCurrencies().getEndpoint();

        log.info("Fetching list of supported currencies");

        Map<String, String> currencies = webClient.get()
                .uri(endpoint)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .onErrorMap(e -> WebClientErrorHandler.mapException(e, endpoint))
                .block();

        CurrenciesResponse response = CurrenciesResponse.builder()
                .currencies(currencies)
                .build();

        // Store in DataStore
        if (response != null) {
            dataStoreService.store(getResourceType(), response);
        }

        return response;
    }

    @Override
    public Object getData() {
        return dataStoreService.get(getResourceType());
    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.CURRENCIES;
    }
}
