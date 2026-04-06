package com.example.allotest.strategy.impl;

import java.util.Map;
import org.springframework.stereotype.Component;
import com.example.allotest.dto.LatestRateResponse;
import com.example.allotest.service.DataStoreService;
import com.example.allotest.strategy.IDRDataFetcher;
import com.example.allotest.util.SpreadCalculator;

@Component("latest_idr_rates")
public class LatestRateStrategy implements IDRDataFetcher {

    private final DataStoreService store;

    public LatestRateStrategy(DataStoreService store) {
        this.store = store;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public Object getData() {
        Map<String, Object> data = (Map<String, Object>) store.get("latest_idr_rates");
        String base = (String) data.get("base");
        String date = (String) data.get("date");
        Map<String, Double> rates = (Map<String, Double>) data.get("rates");
        double usd = rates.get("USD");
        double spread = SpreadCalculator.calculate("WahyuGiri04");
        double result = (1/usd) * (1 + spread);
        return new LatestRateResponse(base, date, rates, result);
    }
}
