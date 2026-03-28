package com.allobank.allo_backend_test.finance.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.Map;

public record HistoricalRatesDto(
        Double amount,
        String base,
        @JsonProperty("start_date") LocalDate startDate,
        @JsonProperty("end_date") LocalDate endDate,
        Map<String, Map<String, Double>> rates
) {}