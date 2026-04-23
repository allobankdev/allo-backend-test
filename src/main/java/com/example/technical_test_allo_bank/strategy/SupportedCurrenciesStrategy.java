package com.example.technical_test_allo_bank.strategy;

import com.example.technical_test_allo_bank.client.FrankfurterClient;
import org.springframework.stereotype.Component;

@Component
public class SupportedCurrenciesStrategy implements FinanceStrategy {

    private final FrankfurterClient client;

    public SupportedCurrenciesStrategy(FrankfurterClient client) {
        this.client = client;
    }

    @Override
    public String getType() {
        return "supported_currencies";
    }

    @Override
    public Object load() {
        return client.supportedCurrencies();
    }
}
