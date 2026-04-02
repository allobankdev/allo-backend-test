package com.example.idrapi.service;

import com.example.idrapi.model.FinanceDataResponse;
import com.example.idrapi.strategy.IDRDataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FinanceDataService {

    private final Map<String, IDRDataFetcher> fetcherRegistry;
    private final FinanceDataStore dataStore;

    public FinanceDataService(List<IDRDataFetcher> fetchers, FinanceDataStore dataStore) {
        this.fetcherRegistry = fetchers.stream()
                .collect(Collectors.toMap(IDRDataFetcher::getResourceType, Function.identity()));
        this.dataStore = dataStore;
        log.info("Registered IDRDataFetcher strategies: {}", this.fetcherRegistry.keySet());
    }

    public void loadAll() {
        log.info("Starting startup data load for {} resource types...", fetcherRegistry.size());

        fetcherRegistry.forEach((resourceType, fetcher) -> {
            try {
                log.info("Loading resourceType: '{}'", resourceType);
                List<Map<String, Object>> results = fetcher.fetch();
                FinanceDataResponse response = new FinanceDataResponse(resourceType, Instant.now(), results);
                dataStore.put(resourceType, response);
            } catch (Exception ex) {
                log.error("Failed to load resourceType '{}': {}", resourceType, ex.getMessage(), ex);
            }
        });

        dataStore.seal();
        log.info("Startup data load complete.");
    }

    public Optional<FinanceDataResponse> getData(String resourceType) {
        return dataStore.get(resourceType);
    }

    public java.util.Set<String> getRegisteredResourceTypes() {
        return fetcherRegistry.keySet();
    }
}
