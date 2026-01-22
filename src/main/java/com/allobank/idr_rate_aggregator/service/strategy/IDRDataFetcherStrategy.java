package com.allobank.idr_rate_aggregator.service.strategy;

/**
 * Strategy interface for fetching different types of IDR data.
 * 
 * This follows the Strategy Design Pattern to eliminate conditional logic
 * in the controller/service layer. Each implementation handles one specific
 * resource type.
 * 
 * Implementations:
 * - LatestIDRRatesStrategy: Fetches latest rates with spread calculation
 * - HistoricalIDRUSDStrategy: Fetches historical time series data
 * - SupportedCurrenciesStrategy: Fetches list of supported currencies
 */
public interface IDRDataFetcherStrategy {
    
    /**
     * Fetch data from the external API and transform as needed.
     * 
     * @return the fetched and transformed data
     * @throws RuntimeException if data fetching fails
     */
    Object fetchData();
    
    /**
     * Get the resource type identifier for this strategy.
     * This is used to map incoming requests to the correct strategy.
     * 
     * Examples: "latest_idr_rates", "historical_idr_usd", "supported_currencies"
     * 
     * @return the resource type string
     */
    String getResourceType();
}
