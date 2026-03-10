package com.allo.bank.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.allo.bank.dto.FinanceDataItem;
import com.allo.bank.exception.ResourceTypeNotFoundException;
import com.allo.bank.service.store.InMemoryFinanceDataStore;
import com.allo.bank.strategy.IDRDataFetcher;

@Service
public class DefaultFinanceDataService implements FinanceDataService {

    private final InMemoryFinanceDataStore dataStore;
    private final Map<String, IDRDataFetcher> fetchers;

    public DefaultFinanceDataService(InMemoryFinanceDataStore dataStore, List<IDRDataFetcher> fetchers) {
        this.dataStore = dataStore;
        this.fetchers = fetchers.stream()
            .collect(Collectors.toUnmodifiableMap(IDRDataFetcher::resourceType, Function.identity()));
    }

    @Override
    public List<FinanceDataItem> getByResourceType(String resourceType) {
        if (!fetchers.containsKey(resourceType)) {
            throw new ResourceTypeNotFoundException(resourceType);
        }
        return dataStore.getByResourceType(resourceType);
    }

    public Map<String, IDRDataFetcher> getFetchers() {
        return fetchers;
    }
}
