package com.allo.finance.model;

import java.util.Map;

public class CurrencyResponse {

    private Map<String, String> currencies;

    public CurrencyResponse() {}

    public CurrencyResponse(Map<String, String> currencies) {
        this.currencies = currencies;
    }

    public Map<String, String> getCurrencies() {
        return currencies;
    }
}
