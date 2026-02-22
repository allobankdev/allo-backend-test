package com.allobank.finance.strategy;

import com.allobank.finance.client.FrankfurterClient;
import org.springframework.stereotype.Component;

@Component
public class HistoricalIdrUsdFetcher implements IDRDataFetcher {

    private final FrankfurterClient frankfurterClient;

    public HistoricalIdrUsdFetcher(FrankfurterClient frankfurterClient) {
        this.frankfurterClient = frankfurterClient;
    }

    @Override
    public String getResourceType() {
        return "historical_idr_usd";
    }

    @Override
    public Object fetch() {
        return frankfurterClient.getHistoricalIdrUsd();
    }
}
