package com.allobank.finance.service.strategy;

import org.springframework.stereotype.Component;
import com.allobank.finance.client.FrankfurterClient;
import com.allobank.finance.dto.LatestRateResponse;

@Component
public class LatestIdrRatesFetcher implements IDRDataFetcher {

    private final FrankfurterClient client;

    private static final String GITHUB_USERNAME = "bayufadhlurrohmanhasmi";

    public LatestIdrRatesFetcher(FrankfurterClient client) {
        this.client = client;
    }

    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }

    @Override
    public Object fetch() {
        LatestRateResponse response = client.getLatestRates();

        double rateUsd = response.getRates().get("USD");
        double spreadFactor = calculateSpreadFactor();
        double usdBuySpreadIdr =
                (1 / rateUsd) * (1 + spreadFactor);

        response.setUsdBuySpreadIdr(usdBuySpreadIdr);
        response.setSpreadFactor(spreadFactor);
        return response;
    }

    private double calculateSpreadFactor() {
        int sum = GITHUB_USERNAME.chars().sum();
        return (sum % 1000) / 100000.0;
    }
}

