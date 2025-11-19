package com.allo.test.modules.finance.service.impl;

import com.allo.test.modules.finance.enums.ResourceType;
import com.allo.test.modules.finance.service.DataStoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe implementation of DataStoreService for IDR exchange rate data.
 * <p>
 * This store holds data fetched at application startup from the Frankfurter API.
 * Data is stored once and served many times without modification.
 */
@Slf4j
@Service
public class DataStoreServiceImpl implements DataStoreService {

    private final ConcurrentHashMap<String, Object> dataStore = new ConcurrentHashMap<>();

    @Override
    public <T> void store(ResourceType type, T data) {
        log.info("Storing {} in memory", type.getKey());
        dataStore.put(type.getKey(), data);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(ResourceType type) {
        return (T) dataStore.get(type.getKey());
    }

    @Override
    public boolean isDataLoaded() {
        return dataStore.containsKey(ResourceType.LATEST_RATES.getKey())
                && dataStore.containsKey(ResourceType.HISTORICAL_RATES.getKey())
                && dataStore.containsKey(ResourceType.CURRENCIES.getKey());
    }

    @Override
    public void clearAll() {
        log.warn("Clearing all stored exchange rate data");
        dataStore.clear();
    }
}
