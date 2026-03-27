package com.allobank.allo_backend_test.finance.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.Map;

public record LatestRatesModel(
        Double amount,
        String base,
        LocalDate date,
        Map<String, Double> rates,
        @JsonProperty("USD_BuySpread_IDR") Double usdBuySpreadIdr
) implements FinanceResource {
    @Override
    public String resourceType() { return "latest_idr_rates"; }
}