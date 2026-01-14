package com.allobank.finance.strategy;

import org.springframework.stereotype.Component;

@Component
public class HistoricalIdrUsdStrategy implements IDRDataFetcher{

    @Override
    public Object fetch() {
        return "historical_idr_usd";
    }

    @Override
    public String getResourceType() {
        
        return null;
    }

    
}
