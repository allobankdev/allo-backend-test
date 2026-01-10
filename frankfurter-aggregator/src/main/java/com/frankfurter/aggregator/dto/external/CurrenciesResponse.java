package com.frankfurter.aggregator.dto.external;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import java.util.HashMap;
import java.util.Map;

public class CurrenciesResponse {
    private Map<String, String> currencies = new HashMap<>();

    @JsonAnySetter
    public void setCurrency(String code, String name) {
        currencies.put(code, name);
    }

    public Map<String, String> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(Map<String, String> currencies) {
        this.currencies = currencies;
    }
}
