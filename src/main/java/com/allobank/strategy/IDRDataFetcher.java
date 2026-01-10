package com.allobank.strategy;

/**
 * Strategy interface for fetching and transforming IDR-related data
 * from the Frankfurter API.
 */
public interface IDRDataFetcher {
    
    /**
     * Fetch and transform data for a specific resource type.
     * @return the transformed data as an Object
     * @throws Exception if data fetching fails
     */
    Object fetchData() throws Exception;
    
    /**
     * Get the name of the strategy.
     * @return resource type identifier
     */
    String getResourceType();
}
