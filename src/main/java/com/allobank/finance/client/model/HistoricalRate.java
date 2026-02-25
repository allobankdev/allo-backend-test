package com.allobank.finance.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Map;

public record HistoricalRate(
        @JsonProperty("amount")
        BigDecimal amount,

        @JsonProperty("base")
        String base,

        @JsonProperty("start_date")
        String startDate,

        @JsonProperty("end_date")
        String endDate,

        @JsonProperty("rates")
        Map<String, Map<String, BigDecimal>> rates
) {
}