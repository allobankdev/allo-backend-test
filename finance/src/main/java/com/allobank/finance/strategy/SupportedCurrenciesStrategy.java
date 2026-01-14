package com.allobank.finance.strategy;

import org.springframework.stereotype.Component;

import com.allobank.finance.client.FrankfurterClient;

@Component
public class SupportedCurrenciesStrategy implements IDRDataFetcher {

    private final FrankfurterClient client;

    public SupportedCurrenciesStrategy(FrankfurterClient client) {
        this.client = client;
    }

    @Override
    public Object fetch() {
        return client.getSupportedCurrencies();
    }

    @Override
    public String getResourceType() {
        return "supported_currencies";
    }

}
