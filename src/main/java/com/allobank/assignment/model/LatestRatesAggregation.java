package com.allobank.assignment.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public record LatestRatesAggregation(
     BigDecimal amount,
     String base,
     LocalDate date,
     Map<String, BigDecimal> rates,
     @JsonProperty("USD_BuySpread_IDR") BigDecimal usdBuySpreadIdr) {
}
