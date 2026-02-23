package com.allobank.finance.idrrateaggregator.startegy.impl;

import com.allobank.finance.idrrateaggregator.client.FrankfurterClient;
import com.allobank.finance.idrrateaggregator.model.dto.LatestRatesResponse;
import com.allobank.finance.idrrateaggregator.startegy.IDRDataFetcher;
import com.allobank.finance.idrrateaggregator.util.SpreadCalculator;
import com.allobank.finance.idrrateaggregator.model.dto.FinanceResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class LatestIdrRatesFetcher implements IDRDataFetcher {

    private static final String GITHUB_USERNAME = "IlhamX7";

    private final FrankfurterClient client;

    public LatestIdrRatesFetcher(FrankfurterClient client) {
        this.client = client;
    }

    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }

    @Override
    public List<FinanceResponse> fetch() {

        LatestRatesResponse response = client.getLatestIdrRates();

        Double usdRate = Optional.ofNullable(response.getRates().get("USD"))
                .orElseThrow(() -> new IllegalStateException("USD rate not found"));

        double spreadFactor =
                SpreadCalculator.calculateSpreadFactor(GITHUB_USERNAME);

        double usdBuySpreadIdr =
                (1 / usdRate) * (1 + spreadFactor);

        return List.of(
                new FinanceResponse("base", response.getBase()),
                new FinanceResponse("date", response.getDate()),
                new FinanceResponse("usd_rate", usdRate),
                new FinanceResponse("spread_factor", spreadFactor),
                new FinanceResponse("USD_BuySpread_IDR", usdBuySpreadIdr)
        );
    }
}
