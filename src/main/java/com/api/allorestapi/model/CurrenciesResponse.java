package com.api.allorestapi.model;

import lombok.Data;

import java.util.Map;

@Data
public class CurrenciesResponse {

    private Map<String, String> currencies;
}
