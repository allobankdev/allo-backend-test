package com.app.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.List;
import java.util.*;

public class SupportedCurrenciesResponseDto {
    private Map<String, String> currencies = new HashMap<>();

    @JsonAnySetter
    public void addCurrency(String code, String name) {
        currencies.put(code, name);
    }

    public Map<String, String> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(Map<String, String> currencies) {
        this.currencies = currencies;
    }
}
