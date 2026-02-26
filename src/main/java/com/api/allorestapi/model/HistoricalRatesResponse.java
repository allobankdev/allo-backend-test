package com.api.allorestapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
public class HistoricalRatesResponse {

    private String base;
    private String amount;

    @JsonProperty("start_date")
    private LocalDate startDate;

    @JsonProperty("end_date")
    private LocalDate endDate;

    private Map<LocalDate, Map<String, BigDecimal>> rates;
}
