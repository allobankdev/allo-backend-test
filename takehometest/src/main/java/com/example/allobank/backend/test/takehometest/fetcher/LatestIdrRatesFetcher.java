package com.example.allobank.backend.test.takehometest.fetcher;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.allobank.backend.test.takehometest.client.FrankfurterClient;
import com.example.allobank.backend.test.takehometest.spread.SpreadConfig;

@Component
public class LatestIdrRatesFetcher implements DataFetcher {

    private final SpreadConfig spreadConfig;
    private final FrankfurterClient client;

    public LatestIdrRatesFetcher(FrankfurterClient client, SpreadConfig spreadConfig) {
        this.client = client;
        this.spreadConfig = spreadConfig;
    }

    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }

    @Override
    public List<Object> fetchData() {
        Map<String, Object> response = client.getLatestIdrRates();
        Map<String, Double> rates = (Map<String, Double>) response.get("rates");

        double usdRate = rates.get("USD");
        double spreadFactor = spreadConfig.getSpreadFactor();

        double usdBuySpreadIdr = (1 / usdRate) * (1 + spreadFactor);

        Map<String, Object> result = new HashMap<>(response);
        result.put("USD_BuySpread_IDR", usdBuySpreadIdr);

        return List.of(Collections.unmodifiableMap(result));
    }

}
