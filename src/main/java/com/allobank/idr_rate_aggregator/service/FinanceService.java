package com.allobank.idr_rate_aggregator.service;

import com.allobank.idr_rate_aggregator.model.FinanceData;
import com.allobank.idr_rate_aggregator.store.FinanceDataStore;
import com.allobank.idr_rate_aggregator.strategy.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FinanceService {

    private final FinanceDataStore dataStore;
    private final List<IDRDataFetcher> fetchers;

    public void initializeData() {
        log.info("Starting data initialization for all resources...");

        Map<String, FinanceData> dataMap = new HashMap<>();

        for (IDRDataFetcher fetcher : fetchers) {
            try {
                log.info("Fetching resource: {}", fetcher.getResourceType());
                FinanceData data = fetcher.fetch();
                dataMap.put(fetcher.getResourceType(), data);
                log.info("Successfully fetched: {}", fetcher.getResourceType());
            } catch (Exception e) {
                log.error("Failed to fetch resource: {}. Error: {}",
                        fetcher.getResourceType(), e.getMessage());
                throw new RuntimeException(
                        "Startup data initialization failed for: " + fetcher.getResourceType(), e);
            }
        }

        dataStore.loadData(dataMap);
        log.info("All resources initialized successfully.");
    }

    public Optional<FinanceData> getByResourceType(String resourceType) {
        return Optional.ofNullable(dataStore.get(resourceType));
    }

    public boolean isDataLoaded() {
        return dataStore.isLoaded();
    }
}