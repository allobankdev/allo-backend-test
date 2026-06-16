package com.example.allobank.strategy;

/**
 * Strategy interface for fetching IDR-related data
 * based on resource type.
 *
 * Each implementation handles exactly ONE resource.
 */
public interface IDRDataFetcher {

    /**
     * Identifier used by controller / startup runner to select the correct strategy.
  
     * Examples:
     * - "latest_idr_rates"
     * - "historical_idr_usd"
     * - "supported_currencies"
     */
    String getResourceType();

    /**
     * Fetches data from external API and applies any required transformation.
     
     * @return final response object for this resource
     */
    Object fetch();
}