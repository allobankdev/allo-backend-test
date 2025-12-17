package com.allo.finance.service;

import com.allo.finance.strategy.IDRDataFetcher;

import java.util.Map;

public class FinanceDataService {

    private final Map<String, IDRDataFetcher> strategies;
    private final InMemoryStore store;

    public FinanceDataService(Map<String, IDRDataFetcher> strategies, InMemoryStore store) {
        this.strategies = strategies;
        this.store = store;
    }

    public void loadAll() {
        strategies.forEach((key, strategy) ->
                store.put(key, strategy.fetch()));
    }

    public Object get(String resourceType) {
        return store.get(resourceType);
    }
}
