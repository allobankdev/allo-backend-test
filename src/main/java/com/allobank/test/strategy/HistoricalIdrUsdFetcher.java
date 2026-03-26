package com.allobank.test.strategy;

import com.allobank.test.client.FrankfurterClient;
import org.springframework.stereotype.Component;

@Component
public class HistoricalIdrUsdFetcher implements IDRDataFetcher {

    private final FrankfurterClient frankfurterClient;

    public HistoricalIdrUsdFetcher(FrankfurterClient frankfurterClient) {
        this.frankfurterClient = frankfurterClient;
    }

    @Override
    public String resourceType() {
        return "historical_idr_usd";
    }

    @Override
    public Object fetch() {
        return frankfurterClient.fetchHistoricalIdrUsd();
    }
}
