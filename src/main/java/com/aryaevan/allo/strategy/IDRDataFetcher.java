package com.aryaevan.allo.strategy;

/**
 * Strategy interface for fetching different types of IDR-related financial data.
 * Implementations follow the Strategy Design Pattern to handle different resource types.
 */
public interface IDRDataFetcher {
    /**
     * Fetches data for a specific resource type.
     * @return The aggregated data for the resource
     */
    Object fetchData();
    
    /**
     * Returns the resource type this strategy handles.
     * @return The resource type identifier
     */
    String getResourceType();
}
