package com.allobank.allobank_api.strategy.impl;

import java.util.Map;

import com.allobank.allobank_api.client.frankfurter.FrankfurterClient;
import com.allobank.allobank_api.dto.oas.CurrencyOas;
import com.allobank.allobank_api.strategy.IDRDataFetcher;

public class SupportedCurrenciesImpl implements IDRDataFetcher<CurrencyOas.Response> {

    private final FrankfurterClient client;

    public SupportedCurrenciesImpl(FrankfurterClient client) {
        this.client = client;
    }

    @Override
    public String getType() {
        return "supported_currencies";
    }

    @Override
    public CurrencyOas.Response fetchAndTransform() {
        Map<String, String> currencies = client.getCurrencies();
        return new CurrencyOas.Response(currencies);
    }
    
}
