package com.finance.strategy;

import org.springframework.stereotype.Component;

import com.finance.service.FinanceCacheService;

@Component
public class CurrencyListStrategy implements FinanceStrategy {

    private final FinanceCacheService cacheService;

    public CurrencyListStrategy(FinanceCacheService cacheService) {
        this.cacheService = cacheService;
    }
    
    @Override
    public String getType() {
        return "currencies";
    }

    @Override
    public Object execute() {
        return cacheService.get("supported_currencies");
    }
}
