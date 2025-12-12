package com.allobank.finance.strategy;

import org.springframework.stereotype.Component;

import com.allobank.finance.service.HistoricalDataService;

@Component("historical_idr_usd")
public class HistoricalIDRUSDStrategy implements IDRDataFetcher {

    private final HistoricalDataService historicalDataService;

    public HistoricalIDRUSDStrategy(HistoricalDataService historicalDataService) {
        this.historicalDataService = historicalDataService;
    }

    @Override
    public Object fetchData() {
        return historicalDataService.fetchHistoricalData();
    }
}
