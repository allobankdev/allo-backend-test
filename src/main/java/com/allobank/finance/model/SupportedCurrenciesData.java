package com.allobank.finance.model;

import lombok.Builder;

import java.util.Map;

@Builder(toBuilder = true)
public record SupportedCurrenciesData(
        Map<String, String> currencies,
        int count
) implements FinanceData {
}
