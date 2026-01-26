package com.example.AlloBank.response;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import java.util.HashMap;
import java.util.Map;

public class CurrenciesResponse {

    private Map<String, String> currencies = new HashMap<>();

    @JsonAnySetter
    public void add(String key, String value) {
        currencies.put(key, value);
    }


    public Map<String, String> getCurrencies() {
        return currencies;

    }
    public void setCurrencies(Map<String, String> currencies) {
        this.currencies = currencies;
    }

}
