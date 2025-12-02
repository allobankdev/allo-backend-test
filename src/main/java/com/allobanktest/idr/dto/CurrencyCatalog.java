package com.allobanktest.idr.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyCatalog {
    private final Map<String, String> currencies = new LinkedHashMap<>();

    @JsonAnySetter
    public void add(String code, String name) {
        currencies.put(code, name);
    }
}

