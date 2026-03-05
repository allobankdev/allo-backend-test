package com.allobank.finance.service;

import com.allobank.finance.model.FinanceDataResponse;
import com.allobank.finance.strategy.IDRDataFetcher;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class FinanceDataService {

    private static final Logger log = Logger.getLogger(FinanceDataService.class.getName());

    private final Map<String, IDRDataFetcher> strategyMap;
    private final InMemoryFinanceStore store;

    public FinanceDataService(Map<String, IDRDataFetcher> strategyMap, InMemoryFinanceStore store) {
        this.strategyMap = strategyMap;
        this.store = store;
    }

    public Optional<FinanceDataResponse> getDataForResource(String resourceType) {
        return store.get(resourceType)
                .map(data -> {
                    log.info(String.format("[FinanceDataService] Serving %d record untuk '%s'",
                            data.size(), resourceType));
                    return FinanceDataResponse.builder()
                            .resourceType(resourceType)
                            .results(data)
                            .totalCount(data.size())
                            .fetchedAt("startup")
                            .build();
                });
    }

    public Map<String, IDRDataFetcher> getStrategyMap() {
        return strategyMap;
    }

    public InMemoryFinanceStore getStore() {
        return store;
    }

    public boolean isKnownResourceType(String resourceType) {
        return strategyMap.containsKey(resourceType);
    }
}