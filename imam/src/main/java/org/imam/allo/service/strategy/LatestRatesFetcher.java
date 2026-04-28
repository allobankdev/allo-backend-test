package org.imam.allo.service.strategy;

import org.imam.allo.client.FrankfurterClient;
import org.springframework.stereotype.Service;

@Service
public class LatestRatesFetcher implements IDRDataFetcher{
    private final FrankfurterClient client;

    public LatestRatesFetcher(FrankfurterClient client) {
        this.client = client;
    }

    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }

    @Override
    public Object fetchData() {
        return client.getLatestRates();
    }
}
