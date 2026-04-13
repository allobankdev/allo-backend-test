package com.allobank.test.service;

import com.allobank.test.exception.ResourceTypeNotSupportedException;
import com.allobank.test.store.FinanceDataStore;
import com.allobank.test.strategy.IDRDataFetcherRegistry;
import org.springframework.stereotype.Service;

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
