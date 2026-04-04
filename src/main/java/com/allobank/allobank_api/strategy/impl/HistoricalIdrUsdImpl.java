package com.allobank.allobank_api.strategy.impl;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.allobank.allobank_api.client.frankfurter.FrankfurterClient;
import com.allobank.allobank_api.dto.oas.HistoricalOas;
import com.allobank.allobank_api.strategy.IDRDataFetcher;

@Component
public class HistoricalIdrUsdImpl implements IDRDataFetcher<HistoricalOas.Response>{
    private final FrankfurterClient client;

    public HistoricalIdrUsdImpl(FrankfurterClient client) {
        this.client = client;
    }

    @Override
    public String getType() {
        return "historical_idr_usd";
    }

    @Override
    public HistoricalOas.Response fetchAndTransform() {
        Map<String, Object> response = client.getHistoricalRates();

        Map<String, Map<String, Double>> rates =
                (Map<String, Map<String, Double>>) response.get("rates");

        return new HistoricalOas.Response(rates);
    }
}
