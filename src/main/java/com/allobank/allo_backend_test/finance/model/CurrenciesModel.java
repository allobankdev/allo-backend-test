package com.allobank.allo_backend_test.finance.model;

import java.util.Map;

public record CurrenciesModel(
        Map<String, String> currencies
) implements FinanceResource {
    @Override
    public String resourceType() { return "supported_currencies"; }
}