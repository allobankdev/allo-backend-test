package com.allobank.finance.service.strategy;

import org.springframework.stereotype.Component;

import com.allobank.finance.client.FrankfurterClient;


@Component
public class HistoricalIdrUsdFetcher implements IDRDataFetcher {

    private final FrankfurterClient client;

    public HistoricalIdrUsdFetcher(FrankfurterClient client) {
        this.client = client;
    }

    @Override
    public String getResourceType() {
        return "historical_idr_usd";
    }

    @Override
    public Object fetch() {
        return client.getHistoricalRates();
    }
}
