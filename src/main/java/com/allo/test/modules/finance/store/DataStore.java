package com.allo.test.modules.finance.store;

import com.allo.test.modules.finance.enums.ResourceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe data store for IDR exchange rate data.
 * <p>
 * This store holds data fetched at application startup from the Frankfurter API.
 * Data is stored once and served many times without modification.
 */
@Slf4j
@Component
public class DataStore {

    private final ConcurrentHashMap<String, Object> dataStore = new ConcurrentHashMap<>();

    /**
     * Stores data in memory for the specified resource type.
     *
     * @param type the resource type to store
     * @param data the data to store
     * @param <T>  the type of data being stored
     */
    public <T> void store(ResourceType type, T data) {
        log.info("Storing {} in memory", type.getDescription());
        dataStore.put(type.getKey(), data);
    }

    /**
     * Retrieves data from memory.
     *
     * @param type the resource type to retrieve
     * @param <T>  the type of data being retrieved
     * @return the stored data, or null if not available
     */
    @SuppressWarnings("unchecked")
    public <T> T get(ResourceType type) {
        return (T) dataStore.get(type.getKey());
    }

    /**
     * Checks if all three resources have been loaded into the store.
     *
     * @return true if all data is available, false otherwise
     */
    public boolean isDataLoaded() {
        return dataStore.containsKey(ResourceType.LATEST_RATES.getKey())
                && dataStore.containsKey(ResourceType.HISTORICAL_RATES.getKey())
                && dataStore.containsKey(ResourceType.CURRENCIES.getKey());
    }

    /**
     * Clears all stored data.
     * <p>
     * Note: This method should only be used for testing or maintenance purposes.
     */
    public void clearAll() {
        log.warn("Clearing all stored exchange rate data");
        dataStore.clear();
    }
}
