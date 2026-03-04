package com.finance.strategy;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.finance.service.FinanceCacheService;
import com.finance.util.SpreadCalculator;

@Component
public class LatestRateStrategy implements FinanceStrategy {

    private final FinanceCacheService cacheService;
    private static final double SPREAD_FACTOR = 
        SpreadCalculator.calculateSpreadFactor("achmadsu");

    public LatestRateStrategy(FinanceCacheService cacheService) {
        this.cacheService = cacheService;
    }
    
    @Override
    public String getType() {
        return "latest";
    }

    @Override
    public Object execute() {
        Map<String, Object> data =
            (Map<String, Object>) cacheService.get("latest_idr_rates");
        Map<String, Double> rates =
            (Map<String, Double>) data.get("rates");
        
        double rateUsd = rates.get("USD");

        double usdBuySpread =
            (1 / rateUsd) * (1 + SPREAD_FACTOR);
        
        Map<String, Object> result = new HashMap<>(data);
        result.put("USD_BuySpread_IDR", usdBuySpread);

        return result;

    }

}
