package com.example.allo.strategy;

import com.example.allo.client.FrankfurterClient;
import org.springframework.stereotype.Component;

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
        return client.getHistoricalRates(
                "2024-01-01", "2024-01-05", "IDR", "USD");
    }
}

