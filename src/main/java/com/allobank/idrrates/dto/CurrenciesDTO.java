package com.allobank.idrrates.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrenciesDTO {
    private Map<String, String> currencies = new HashMap<>();

    @JsonAnySetter
    public void setCurrency(String key, String value) {
        currencies.put(key, value);
    }

    @JsonAnyGetter
    public Map<String, String> getCurrencies() {
        return currencies;
    }
}
