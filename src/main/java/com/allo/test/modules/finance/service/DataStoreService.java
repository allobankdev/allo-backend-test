package com.allo.test.modules.finance.service;

import com.allo.test.modules.finance.enums.ResourceType;

/**
 * Service interface for thread-safe data storage of IDR exchange rate data.
 * <p>
 * This store holds data fetched at application startup from the Frankfurter API.
 * Data is stored once and served many times without modification.
 */
public interface DataStoreService {

    /**
     * Stores data in memory for the specified resource type.
     *
     * @param type the resource type to store
     * @param data the data to store
     * @param <T>  the type of data being stored
     */
    <T> void store(ResourceType type, T data);

    /**
     * Retrieves data from memory.
     *
     * @param type the resource type to retrieve
     * @param <T>  the type of data being retrieved
     * @return the stored data, or null if not available
     */
    <T> T get(ResourceType type);

    /**
     * Checks if all three resources have been loaded into the store.
     *
     * @return true if all data is available, false otherwise
     */
    boolean isDataLoaded();

    /**
     * Clears all stored data.
     * <p>
     * Note: This method should only be used for testing or maintenance purposes.
     */
    void clearAll();
}
