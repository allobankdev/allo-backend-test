package com.allobank.financeapi.service;

import com.allobank.financeapi.model.DataStore;
import com.allobank.financeapi.model.FinanceData;
import com.allobank.financeapi.service.strategy.StrategyRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class FinanceService {

    private final DataStore dataStore;
    private final StrategyRegistry strategyRegistry;

    public Mono<FinanceData> getDataFromStore(String resourceType) {
        if (!dataStore.isInitialized()) {
            return Mono.error(new IllegalStateException("Data store not initialized yet"));
        }

        return Mono.fromCallable(() -> {
            switch (resourceType) {
                case "latest_idr_rates":
                    return FinanceData.builder()
                            .resourceType(resourceType)
                            .data(dataStore.getLatestIdrRates())
                            .build();
                case "historical_idr_usd":
                    return FinanceData.builder()
                            .resourceType(resourceType)
                            .data(dataStore.getHistoricalIdrUsd())
                            .build();
                case "supported_currencies":
                    return FinanceData.builder()
                            .resourceType(resourceType)
                            .data(dataStore.getSupportedCurrencies())
                            .build();
                default:
                    throw new IllegalArgumentException("Unsupported resource type: " + resourceType);
            }
        });
    }
}