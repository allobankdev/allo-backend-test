package com.allobank.backendtest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record HistoricalRatesResponse(
        double amount,
        String base,
        @JsonProperty("start_date") String startDate,
        @JsonProperty("end_date") String endDate,
        Map<String, Map<String, Double>> rates
) {
}
