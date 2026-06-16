package com.allobank.strategy;

import reactor.core.publisher.Mono;

/**
 * Strategy interface for fetching IDR-related data from the Frankfurter API.
 * This interface defines the contract for different data fetching strategies.
 */
public interface IDRDataFetcher {
    
    /**
     * Fetches and transforms data from the external API.
     *
     * @return A Mono containing the fetched and transformed data
     */
    Mono<Object> fetchData();
    
    /**
     * Returns the resource type this strategy handles.
     *
     * @return The resource type identifier
     */
    String getResourceType();
}

