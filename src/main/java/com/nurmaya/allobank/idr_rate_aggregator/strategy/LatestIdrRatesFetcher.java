package com.nurmaya.allobank.idr_rate_aggregator.strategy;

import java.util.List;

import org.springframework.stereotype.Component;

import com.nurmaya.allobank.idr_rate_aggregator.client.FrankfurterClient;
import com.nurmaya.allobank.idr_rate_aggregator.dto.LatestRatesResponse;
import com.nurmaya.allobank.idr_rate_aggregator.util.SpreadFactorCalculator;

@Component("latest_idr_rates")
public class LatestIdrRatesFetcher implements IDRDataFetcher{
    private final FrankfurterClient client;
    private final SpreadFactorCalculator spreadCalculator;

    public LatestIdrRatesFetcher(FrankfurterClient client, SpreadFactorCalculator spreadCalculator) {
        this.client = client;
        this.spreadCalculator = spreadCalculator;
    }

    @Override
    public List<LatestRatesResponse> fetchData() {

        LatestRatesResponse response = client.getLatestIdrRates();
        Double usdRate = response.getRates().get("USD");

        double spreadFactor = spreadCalculator.calculateSpreadFactor();
        double buySpread = (1 / usdRate) * (1 + spreadFactor);

        response.setUsdBuySpreadIdr(buySpread);

        return List.of(response);
    }
}
