package com.allo.test.modules.finance.client.strategy;

import com.allo.test.modules.finance.enums.ResourceType;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Strategy interface for fetching and transforming IDR (Indonesian Rupiah) exchange rate data
 * from the Frankfurter API.
 * <p>
 * Each implementation handles a specific resource type (LATEST_RATES, HISTORICAL_RATES,
 * or CURRENCIES) and is responsible for data fetching, storage, and retrieval.
 */
public interface IDRDataFetcher {

    /**
     * Fetches and transforms data from a specific Frankfurter API endpoint.
     * Also stores the fetched data in the DataStore for later retrieval.
     * <p>
     * Implementations should handle the HTTP request, response parsing, any
     * required data transformations (e.g., calculating USD_BuySpread_IDR for
     * latest rates), and storing the result.
     * <p>
     * This method is called once at application startup by the StartupLoader.
     *
     * @param webClient the WebClient instance to use for the HTTP request
     * @return the transformed response data
     */
    Object fetchData(WebClient webClient);

    /**
     * Retrieves the previously fetched and stored data from the DataStore.
     * <p>
     * This method is called by the service layer to serve API requests.
     * Returns the data that was fetched and stored by fetchData().
     *
     * @return the stored data, or null if not available
     */
    Object getData();

    /**
     * Returns the resource type identifier that this strategy handles.
     * <p>
     * This value is used for type-safe resource routing throughout the application.
     * <p>
     * Valid resource types:
     * <ul>
     *   <li>LATEST_RATES - Latest exchange rates with USD spread calculation</li>
     *   <li>HISTORICAL_RATES - Historical IDR to USD rates for a date range</li>
     *   <li>CURRENCIES - List of all supported currency symbols</li>
     * </ul>
     *
     * @return the ResourceType enum value
     */
    ResourceType getResourceType();
}
