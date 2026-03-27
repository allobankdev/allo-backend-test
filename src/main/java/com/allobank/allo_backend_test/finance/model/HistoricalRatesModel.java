package com.allobank.allo_backend_test.finance.model;

import java.time.LocalDate;
import java.util.Map;

public record HistoricalRatesModel(
        Double amount,
        String base,
        LocalDate startDate,
        LocalDate endDate,
        Map<String, Map<String, Double>> rates
) implements FinanceResource {
    @Override
    public String resourceType() { return "historical_idr_usd"; }
}