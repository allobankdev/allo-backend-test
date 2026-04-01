package org.imam.allo.service.strategy;

import org.imam.allo.client.FrankfurterClient;
import org.springframework.stereotype.Service;

@Service
public class HistoricalFetcher implements IDRDataFetcher{
    private final FrankfurterClient client;

    public HistoricalFetcher(FrankfurterClient client) {
        this.client = client;
    }

    @Override
    public String getResourceType() {
        return "historical_idr_usd";
    }

    @Override
    public Object fetchData() {
        return client.getHistorical();
    }
}

