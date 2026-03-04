package com.finance.strategy;

import org.springframework.stereotype.Component;

import com.finance.service.FinanceCacheService;

@Component
public class LatestRateStrategy implements FinanceStrategy {

    private final FinanceCacheService cacheService;

    public LatestRateStrategy(FinanceCacheService cacheService) {
        this.cacheService = cacheService;
    }
    
    @Override
    public String getType() {
        return "latest";
    }

    @Override
    public Object execute() {
        return cacheService.get("latest_idr_rates");
    }

}
