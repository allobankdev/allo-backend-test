package com.allobank.finance.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Map;

public record LatestRate(
        @JsonProperty("amount")
        BigDecimal amount,

        @JsonProperty("base")
        String base,

        @JsonProperty("date")
        String date,

        @JsonProperty("rates")
        Map<String, BigDecimal> rates
) {
}