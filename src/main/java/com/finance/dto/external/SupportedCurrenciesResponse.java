package com.finance.dto.external;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class SupportedCurrenciesResponse {
    private Map<String, String> supportedCurrencies;

    @JsonAnySetter
    public void addCurrency(String code, String name) {
        if (supportedCurrencies == null) supportedCurrencies = new HashMap<>();
        supportedCurrencies.put(code, name);
    }
}
