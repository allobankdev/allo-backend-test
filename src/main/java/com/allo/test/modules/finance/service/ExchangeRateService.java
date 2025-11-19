package com.allo.test.modules.finance.service;

/**
 * Service interface for accessing IDR exchange rate data from the in-memory store.
 * <p>
 * Delegates all data retrieval to strategy implementations.
 * Each strategy is self-contained and handles its own data lifecycle.
 */
public interface ExchangeRateService {

    /**
     * Unified method to retrieve exchange rate data by resource type.
     * <p>
     * Valid resource types:
     * <ul>
     *   <li>latest_idr_rates - Latest IDR exchange rates with USD buy spread</li>
     *   <li>historical_idr_usd - Historical IDR to USD rates for a date range</li>
     *   <li>supported_currencies - List of all supported currency symbols</li>
     * </ul>
     *
     * @param resourceType the resource type identifier
     * @return Object containing the requested data
     */
    Object getData(String resourceType);

    /**
     * Checks if all exchange rate data resources are loaded and available.
     *
     * @return true if all data is loaded, false otherwise
     */
    boolean isDataAvailable();
}
