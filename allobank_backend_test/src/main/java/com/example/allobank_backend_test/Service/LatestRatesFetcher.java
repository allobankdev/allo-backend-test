package com.example.allobank_backend_test.Service;

import com.example.allobank_backend_test.Client.FrankfurterClient;
import com.example.allobank_backend_test.DTO.LatestRatesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("latest_idr_rates")
@RequiredArgsConstructor
public final class LatestRatesFetcher implements IDRDataFetcher{
    private static final String username = "khoirulromadhon";
    private final FrankfurterClient client;

    @Override
    public String getType() {
        return "latest_idr_rates";
    }

    @Override
    public Object fetch() {
        var response = client.getLatestRates();
        double rateUsd = response.rates().getOrDefault("USD", 0.0);
        double spreadFactor = calculatedSpread(username);
        double usdBuySpread = (1 / rateUsd) * (1 + spreadFactor);

        return new LatestRatesResponse(response.rates(), usdBuySpread);
    }

    private double calculatedSpread(String username) {
        int sum = username.toLowerCase()
                .chars()
                .sum();

        return (sum % 1000) / 100000.0;
    }
}
