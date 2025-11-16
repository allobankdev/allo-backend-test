package com.allobank.service;

import com.allobank.config.FrankfurterApiProperties;
import com.allobank.dto.HistoricalRatesResponse;
import com.allobank.enums.ResourceType;
import com.allobank.store.DataStoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Component("historical_idr_usd")
@RequiredArgsConstructor
@Slf4j
public class HistoricalIDRUSDStrategy implements IDRDataFetcher {

    private final WebClient webClient;
    private final FrankfurterApiProperties properties;
    private final DataStoreService dataStoreService;

    @Override
    public Object fetchFromExternalApi() {
        String endpoint = properties.getEndpoints().getHistoricalIdrUsd();
        log.info("Fetching historical IDR-USD rates from: {}", endpoint);


        HistoricalRatesResponse response = webClient.get()
                .uri(endpoint)
                .retrieve()
                .bodyToMono(HistoricalRatesResponse.class)
                .block();

        if (response == null) {
            throw new RuntimeException("Failed to fetch historical rates");
        }

        log.info("Successfully fetched historical data with {} entries",
                Optional.ofNullable(response.getRates()).map(java.util.Map::size).orElse(0));

        return response;
    }

    @Override
    public Object getData() {
        return dataStoreService.getData(getResourceType().getValue());
    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.HISTORICAL_IDR_USD;
    }
}
