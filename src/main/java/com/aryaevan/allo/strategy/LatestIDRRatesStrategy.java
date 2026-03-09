package com.aryaevan.allo.strategy;

import org.springframework.stereotype.Component;

/**
 * Strategy implementation for fetching the latest IDR exchange rates.
 * Handles the /latest?base=IDR resource from Frankfurter API.
 */
@Component
public class LatestIDRRatesStrategy implements IDRDataFetcher {
    
    @Override
    public Object fetchData() {
        // TODO: Implement actual data fetching
        return "LatestIDRRatesStrategy data";
    }
    
    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }
}
