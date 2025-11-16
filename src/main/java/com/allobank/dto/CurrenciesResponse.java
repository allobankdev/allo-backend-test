package com.allobank.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CurrenciesResponse {

    private Map<String, String> currencies = new ConcurrentHashMap<>();

    @JsonAnySetter
    public void addCurrency(String code, String name) {
        currencies.put(code, name);
    }

}
