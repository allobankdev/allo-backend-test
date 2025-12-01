package com.example.idraggregator.dto;

import java.util.Map;

public class SupportedCurrenciesDto {
    private Map<String, String> currencies;

    public SupportedCurrenciesDto() {}

    public Map<String, String> getCurrencies() { return currencies; }
    public void setCurrencies(Map<String, String> currencies) { this.currencies = currencies; }
}
