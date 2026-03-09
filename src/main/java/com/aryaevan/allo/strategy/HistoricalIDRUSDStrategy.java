package com.aryaevan.allo.strategy;

import org.springframework.stereotype.Component;

/**
 * Strategy implementation for fetching historical IDR to USD exchange rates.
 * Handles the time series data from Frankfurter API.
 */
@Component
public class HistoricalIDRUSDStrategy implements IDRDataFetcher {
    
    @Override
    public Object fetchData() {
        // TODO: Implement actual data fetching
        return "HistoricalIDRUSDStrategy data";
    }
    
    @Override
    public String getResourceType() {
        return "historical_idr_usd";
    }
}
