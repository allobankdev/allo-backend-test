package com.example.technical_test_allo_bank.strategy;

import com.example.technical_test_allo_bank.client.FrankfurterClient;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LatestIdrRateStrategy implements FinanceStrategy {

    private final FrankfurterClient client;

    public LatestIdrRateStrategy(FrankfurterClient client) {
        this.client = client;
    }

    @Override
    public String getType() {
        return "latest_idr_rates";
    }

    @Override
    public Object load() {
        Map<String, Object> data = client.latestIdrRates();
        Map<String, Object> rates = (Map<String, Object>) data.get("rates");

        double usd = Double.parseDouble(rates.get("USD").toString());
        rates.put("USD_BuySpread_IDR", usd * 1.02);

        return data;
    }
}
