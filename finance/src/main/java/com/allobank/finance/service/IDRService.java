package com.allobank.finance.service;

import org.springframework.stereotype.Service;
import com.allobank.finance.store.FinanceDataStore;

@Service
public class IDRService {

    private final FinanceDataStore dataStore;

    public IDRService(FinanceDataStore dataStore) {
        this.dataStore = dataStore;
    }

    public Object getData(String resourceType) {
        return dataStore.get(resourceType);
    }
}
