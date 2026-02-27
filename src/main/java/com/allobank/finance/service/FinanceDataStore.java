package com.allobank.finance.service;

import com.allobank.finance.model.response.FinanceDataResponse;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FinanceDataStore {

    private Map<String, FinanceDataResponse> store = Map.of();

    public synchronized void load(Map<String, FinanceDataResponse> data) {
        this.store = Map.copyOf(data);
    }

    public FinanceDataResponse get(String resourceType) {
        return store.get(resourceType);
    }

    public Map<String, FinanceDataResponse> getAll() {
        return store;
    }
}
