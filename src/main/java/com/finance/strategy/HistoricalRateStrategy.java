package com.finance.strategy;

import org.springframework.stereotype.Component;

import com.finance.service.FinanceCacheService;

@Component
public class HistoricalRateStrategy implements FinanceStrategy{
    
    private final FinanceCacheService cacheService;

    public HistoricalRateStrategy(FinanceCacheService cacheService) {
        this.cacheService = cacheService;
    }
    
    @Override
    public String getType() {
        return "historical";
    }

    @Override
    public Object execute() {
        return cacheService.get("historical_idr_usd");
    }
}
