package com.allo.test.service;

import com.allo.test.strategy.IDRDataFetcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FinanceService {
    private final InMemoryDataStore dataStore;
    public FinanceService(InMemoryDataStore dataStore) {
        this.dataStore = dataStore;
    }

    public List<Object> getData(String resourceType) {
        List<Object> data = dataStore.get(resourceType);

        if (data == null) {
            throw new RuntimeException("Resource not found");
        }

        return data;
    }
}
