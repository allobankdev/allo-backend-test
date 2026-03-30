package com.allobank.test.strategy;

/**
 * Strategy interface definition for handling different data resources
 * from the Frankfurter API.
 */
public interface DataFetcherStrategy {
    
    /**
     * @return The canonical identifier matching the path variable (e.g., latest_idr_rates).
     */
    String getResourceType();

    /**
     * Fetches the data from the external API and performs any required 
     * business transformations before storing.
     * 
     * @return Transformed JSON data object (Map, DTO, etc.)
     */
    Object fetchAndTransform();
}
