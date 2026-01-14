package com.allobank.finance.strategy;

import org.springframework.stereotype.Component;

import com.allobank.finance.client.FrankfurterClient;

@Component
public class HistoricalIdrUsdStrategy implements IDRDataFetcher {

    private final FrankfurterClient client;

    public HistoricalIdrUsdStrategy(FrankfurterClient client) {
        this.client = client;
    }

    @Override
    public Object fetch() {
        return client.getHistoricalIdrUsd();
    }

    @Override
    public String getResourceType() {

        return "historical_idr_usd";
    }

}
