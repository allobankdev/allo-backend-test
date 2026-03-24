package com.allobank.test.service;

import com.allobank.test.store.FinanceDataStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinanceDataService {

    private final FinanceDataStore financeDataStore;

    public FinanceDataService(FinanceDataStore financeDataStore) {
        this.financeDataStore = financeDataStore;
    }

    public Object findByResourceType(String resourceType) {
        return financeDataStore.getByResourceType(resourceType);
    }

    public List<String> supportedResourceTypes() {
        return financeDataStore.supportedResourceTypes();
    }
}
