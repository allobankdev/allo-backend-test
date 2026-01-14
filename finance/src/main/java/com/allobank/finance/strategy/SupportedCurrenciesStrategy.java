package com.allobank.finance.strategy;

import org.springframework.stereotype.Component;

@Component
public class SupportedCurrenciesStrategy implements IDRDataFetcher {

    @Override
    public Object fetch() {
        return "supported_currencies";
    }

    @Override
    public String getResourceType() {
        return null;
    }

    
}
