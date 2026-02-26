package com.api.allorestapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
public class LatestRatesResponse {

    private String base;
    private LocalDate date;

    @JsonProperty("rates")
    private Map<String, BigDecimal> rates;
}
