package com.backend.allobank.strategy;

import com.backend.allobank.dto.FrankfurterRatesResponse;
import com.backend.allobank.util.SpreadFactorCalculator;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Component
@Profile("!test")
public class LatestIdrRatesStrategy implements IDRDataFetcher {

    private final WebClient webClient;

    public LatestIdrRatesStrategy(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }

    @Override
    public Object fetchAndTransform() {

        FrankfurterRatesResponse response = webClient.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .bodyToMono(FrankfurterRatesResponse.class)
                .block();

        if (response == null || response.rates() == null) {
            throw new IllegalStateException("Empty response from Frankfurter latest API");
        }

        @SuppressWarnings("unchecked")
        Map<String, Double> rates = (Map<String, Double>) (Map<?, ?>) response.rates();

        Double rateUsd = rates.get("USD");
        if (rateUsd == null) {
            throw new IllegalStateException("USD rate not found in latest rates");
        }

        double spreadFactor = SpreadFactorCalculator.calculate("muhammadakbaar");
        double usdBuySpreadIdr = (1 / rateUsd) * (1 + spreadFactor);

        Map<String, Object> result = new HashMap<>();
        result.put("base", response.base());
        result.put("date", response.date());
        result.put("rates", rates);
        result.put("USD_BuySpread_IDR", usdBuySpreadIdr);

        return result;
    }
}
