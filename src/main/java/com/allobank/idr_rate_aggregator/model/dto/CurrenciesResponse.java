package com.allobank.idr_rate_aggregator.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;

/**
 * DTO untuk response currencies dari Frankfurter API
 * Response berupa Map sederhana: {"USD": "United States Dollar", "EUR": "Euro",
 * ...}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrenciesResponse {

    /**
     * Map dengan key = currency code, value = currency name
     */
    @JsonAnySetter
    private Map<String, String> currencies = new HashMap<>();
}