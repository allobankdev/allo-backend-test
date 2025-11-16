package com.allobank.service;

import com.allobank.enums.ResourceType;

public interface IDRDataFetcher {

    /**
     * Fetches data from the external API for this strategy's resource type.
     * Called ONLY during application startup by ApplicationRunner.
     *
     * @return The processed data as an Object (can be any response type)
     */
    Object fetchFromExternalApi();

    /**
     * Retrieves data from the in-memory data store.
     * Called by controller for each request.
     *
     * @return The cached data from DataStoreService
     */
    Object getData();

    /**
     * Returns the resource type this strategy handles
     *
     * @return The ResourceType enum value
     */
    ResourceType getResourceType();

    /**
     * Returns the Spring bean name for this strategy
     * Used for map-based lookup in the controller
     *
     * @return String identifier for the strategy
     */
    default String getStrategyName() {
        return getResourceType().getValue();
    }
}
