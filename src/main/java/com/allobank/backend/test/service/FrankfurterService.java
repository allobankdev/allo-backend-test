package com.allobank.backend.test.service;

import com.allobank.backend.test.client.FrankfurterClient;
import com.allobank.backend.test.model.CurrenciesResponse;
import com.allobank.backend.test.model.HistoricalRatesResponse;
import com.allobank.backend.test.model.LatestRatesResponse;
import com.allobank.backend.test.util.SpreadCalculator;
import org.springframework.stereotype.Service;

@Service
public class FrankfurterService {

    private final FrankfurterClient client;

    private static final String GITHUB_USERNAME = "danielsinaga";

    public FrankfurterService(FrankfurterClient client) {
        this.client = client;
    }

    public LatestRatesResponse getLatestRates() {

        LatestRatesResponse response = client.getLatestRates();
        Double usdRate = response.getRates().get("USD");
        double spreadFactor =
                SpreadCalculator.calculateSpreadFactor(GITHUB_USERNAME);
        double usdBuySpread =
                (1 / usdRate) * (1 + spreadFactor);

        response.setUsdBuySpreadIdr(usdBuySpread);
        return response;
    }

    public HistoricalRatesResponse getHistoricalRates() {
        return client.getHistoricalRates();
    }

    public CurrenciesResponse getCurrencies() {
        return client.getCurrencies();
    }
}