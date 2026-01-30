package com.example.allobank.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Data;

import java.util.Map;

@Data
public class CurrencyResponseWrapper {
    private Map<String, String> currencies;

    @JsonAnySetter
    public void addCurrency(String code, String name) {
        currencies.put(code, name);
    }
}
