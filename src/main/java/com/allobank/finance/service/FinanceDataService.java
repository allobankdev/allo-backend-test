package com.allobank.finance.service;

import org.springframework.stereotype.Service;

import com.allobank.finance.exception.ResourceTypeNotSupportedException;
import com.allobank.finance.store.FinanceDataStore;
import com.allobank.finance.strategy.IDRDataFetcherRegistry;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class FinanceDataService {

    private final FinanceDataStore financeDataStore;
    private final IDRDataFetcherRegistry registry;

    public FinanceDataService(FinanceDataStore financeDataStore, IDRDataFetcherRegistry registry) {
        this.financeDataStore = financeDataStore;
        this.registry = registry;
    }

    public List<Map<String, Object>> findByResourceType(String resourceType) {
        String normalizedResourceType = resourceType == null
                ? ""
                : resourceType.trim().toLowerCase(Locale.ROOT);

        if (!registry.asMap().containsKey(normalizedResourceType)) {
            throw new ResourceTypeNotSupportedException("Unsupported resourceType: " + resourceType);
        }

        return financeDataStore.getByResourceType(normalizedResourceType);
    }

    public List<String> supportedResourceTypes() {
        return List.copyOf(registry.asMap().keySet());
    }
}
