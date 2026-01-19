package com.example.allo.strategy;

import com.example.allo.client.FrankfurterClient;
import com.example.allo.dto.LatestRatesResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LatestIdrRatesFetcher implements IDRDataFetcher {

    private final FrankfurterClient client;
    private final double spreadFactor;

    public LatestIdrRatesFetcher(
            FrankfurterClient client,
            @Value("${app.github-username}") String username) {

        this.client = client;
        this.spreadFactor = calculateSpread(username.toLowerCase());
    }

    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }

    @Override
    public Object fetch() {
        LatestRatesResponse res = client.getLatestRates("IDR");

        Double usdRate = res.getRates().get("USD");
        double spread = (1 / usdRate) * (1 + spreadFactor);

        res.setUsdBuySpreadIdr(spread);
        return res;
    }

    private double calculateSpread(String username) {
        int sum = username.chars().sum();
        return (sum % 1000) / 100000.0;
    }
}

