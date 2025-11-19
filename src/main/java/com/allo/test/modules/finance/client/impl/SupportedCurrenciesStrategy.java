package com.allo.test.modules.finance.client.impl;

import com.allo.test.configs.properties.FrankfurterApiProperties;
import com.allo.test.modules.finance.client.IDRDataFetcher;
import com.allo.test.modules.finance.dto.res.CurrencyResponse;
import com.allo.test.modules.finance.enums.ResourceType;
import com.allo.test.modules.finance.service.DataStoreService;
import com.allo.test.shared.utils.WebClientErrorHandler;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

/**
 * Strategy implementation for fetching the list of all supported currency symbols
 * from the Frankfurter API.
 * <p>
 * Handles the "supported_currencies" resource type and transforms data to List of
 * CurrencyResponse DTOs for type-safe JSON output.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SupportedCurrenciesStrategy implements IDRDataFetcher {

    private final FrankfurterApiProperties apiProperties;
    private final DataStoreService dataStoreService;

    @Override
    @Retry(name = "frankfurterApi")
    public List<CurrencyResponse> fetchData(WebClient webClient) {
        String endpoint = apiProperties.getCurrencies().getEndpoint();

        log.info("Fetching list of supported currencies");

        Map<String, String> currencies = webClient.get()
                .uri(endpoint)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .onErrorMap(e -> WebClientErrorHandler.mapException(e, endpoint))
                .block();

        // Transform to List of CurrencyResponse DTOs
        List<CurrencyResponse> transformedData = transformToList(currencies);

        // Store in DataStore
        if (!transformedData.isEmpty()) {
            dataStoreService.store(getResourceType(), transformedData);
        }

        return transformedData;
    }

    /**
     * Transforms the raw Map<String, String> to List of CurrencyResponse DTOs.
     * Provides type-safe structure for JSON array output.
     */
    private List<CurrencyResponse> transformToList(Map<String, String> currencies) {
        if (currencies == null) {
            return Collections.emptyList();
        }

        List<CurrencyResponse> transformedList = new ArrayList<>();

        for (Map.Entry<String, String> entry : currencies.entrySet()) {
            CurrencyResponse currencyResponse = CurrencyResponse.builder()
                    .code(entry.getKey())
                    .name(entry.getValue())
                    .build();

            transformedList.add(currencyResponse);
        }

        return transformedList;
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
