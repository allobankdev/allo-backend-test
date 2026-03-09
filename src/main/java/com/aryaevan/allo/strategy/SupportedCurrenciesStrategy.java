package com.aryaevan.allo.strategy;

import org.springframework.stereotype.Component;

/**
 * Strategy implementation for fetching the list of supported currencies.
 * Handles the /currencies resource from Frankfurter API.
 */
@Component
public class SupportedCurrenciesStrategy implements IDRDataFetcher {
    
    @Override
    public Object fetchData() {
        // TODO: Implement actual data fetching
        return "SupportedCurrenciesStrategy data";
    }
    
    @Override
    public String getResourceType() {
        return "supported_currencies";
    }
}
