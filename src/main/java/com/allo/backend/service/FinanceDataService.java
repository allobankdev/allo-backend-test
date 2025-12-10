package com.allo.backend.service;

import com.allo.backend.strategy.IDRDataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FinanceDataService {

    private final InMemoryDataStore dataStore;
    private final Map<String, IDRDataFetcher> strategyMap;

    public FinanceDataService(InMemoryDataStore dataStore, List<IDRDataFetcher> strategies) {
        this.dataStore = dataStore;
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        IDRDataFetcher::getResourceType,
                        Function.identity()
                ));

        log.info("Initialized FinanceDataService with {} strategies: {}",
                strategyMap.size(), strategyMap.keySet());
    }

    public Object getFinanceData(String resourceType) {
        log.info("Retrieving finance data for resource type: {}", resourceType);
        return dataStore.retrieve(resourceType);
    }

    public IDRDataFetcher getStrategy(String resourceType) {
        return strategyMap.get(resourceType);
    }

    public Map<String, IDRDataFetcher> getAllStrategies() {
        return strategyMap;
    }
}
