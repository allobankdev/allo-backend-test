package com.allobank.finance.strategy;

import org.springframework.stereotype.Component;

import com.allobank.finance.service.LatestRatesService;

@Component("latest_idr_rates")
public class LatestIDRRatesStrategy implements IDRDataFetcher {

    private final LatestRatesService latestRatesService;

    public LatestIDRRatesStrategy(LatestRatesService latestRatesService) {
        this.latestRatesService = latestRatesService;
    }

    @Override
    public Object fetchData() {
        return latestRatesService.fetchLatestRates();
    }
}
