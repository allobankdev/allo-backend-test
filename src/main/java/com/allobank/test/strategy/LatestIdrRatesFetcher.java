package com.allobank.test.strategy;

import com.allobank.test.client.FrankfurterClient;
import org.springframework.stereotype.Component;

@Component
public class LatestIdrRatesFetcher implements IDRDataFetcher {

    private final FrankfurterClient frankfurterClient;

    public LatestIdrRatesFetcher(FrankfurterClient frankfurterClient) {
        this.frankfurterClient = frankfurterClient;
    }

    @Override
    public String resourceType() {
        return "latest_idr_rates";
    }

    @Override
    public Object fetch() {
        return frankfurterClient.fetchLatestIdrRates();
    }
}
