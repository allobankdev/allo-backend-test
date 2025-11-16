package com.allobank.service;

import com.allobank.config.FrankfurterApiProperties;
import com.allobank.dto.CurrenciesResponse;
import com.allobank.enums.ResourceType;
import com.allobank.store.DataStoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component("supported_currencies")
@RequiredArgsConstructor
@Slf4j
public class SupportedCurrenciesStrategy implements IDRDataFetcher {

    private final WebClient webClient;
    private final FrankfurterApiProperties properties;
    private final DataStoreService dataStoreService;

    @Override
    public Object fetchFromExternalApi() {
        String endpoint = properties.getEndpoints().getCurrencies();
        log.info("Fetching supported currencies from: {}", endpoint);

        CurrenciesResponse response = webClient.get()
                .uri(endpoint)
                .retrieve()
                .bodyToMono(CurrenciesResponse.class)
                .block();


        if (response == null || response.getCurrencies() == null) {
            throw new RuntimeException("Failed to fetch currencies");
        }

        log.info("Successfully fetched {} currencies", response.getCurrencies().size());

        return response;
    }

    @Override
    public Object getData() {
        return dataStoreService.getData(getResourceType().getValue());
    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.SUPPORTED_CURRENCIES;
    }
}
