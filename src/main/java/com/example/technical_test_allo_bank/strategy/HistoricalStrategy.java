package com.example.technical_test_allo_bank.strategy;

import com.example.technical_test_allo_bank.client.FrankfurterClient;
import org.springframework.stereotype.Component;

@Component
public class HistoricalStrategy implements FinanceStrategy {

    private final FrankfurterClient client;

    public HistoricalStrategy(FrankfurterClient client) {
        this.client = client;
    }

    @Override
    public String getType() {
        return "historical_idr_usd";
    }

    @Override
    public Object load() {
        return client.historicalIdrUsd();
    }
}
