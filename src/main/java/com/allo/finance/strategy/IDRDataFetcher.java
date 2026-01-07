package com.allo.finance.strategy;

public interface IDRDataFetcher {

    /**
     * Identifier resource type
     * contoh:
     * - latest_idr_rates
     * - historical_idr_usd
     * - supported_currencies
     */
    String getResourceType();

    /**
     * Fetch & transform data
     */
    Object fetchData();

}