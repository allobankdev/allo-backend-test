package com.allobank.finance.strategy;

import org.springframework.stereotype.Component;

@Component
public class LatestIdrRatesStrategy implements IDRDataFetcher{

    @Override
    public Object fetch() {
        return "latest_idr_rates";
    }

    @Override
    public String getResourceType() {
        
        return null;
    }

    
}
