package com.allo.backend.model;

import java.util.Map;

public class SupportedCurrenciesResponse {
    private Map<String, String> currencies;

    public SupportedCurrenciesResponse() {}
    public SupportedCurrenciesResponse(Map<String, String> currencies) {
        this.currencies = currencies;
    }
    public Map<String, String> getCurrencies() { return currencies; }
    public void setCurrencies(Map<String, String> currencies) { this.currencies = currencies; }
}
