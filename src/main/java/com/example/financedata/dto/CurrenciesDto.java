package com.example.financedata.dto;

import java.util.Map;

public class CurrenciesDto {
    private Map<String, String> currencies;
    public Map<String, String> getCurrencies() { return currencies; }
    public void setCurrencies(Map<String, String> currencies) { this.currencies = currencies; }
}
