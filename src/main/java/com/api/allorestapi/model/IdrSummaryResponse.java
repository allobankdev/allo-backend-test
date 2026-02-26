package com.api.allorestapi.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
public class IdrSummaryResponse {

    private LocalDate latestDate;
    private Map<String, BigDecimal> latestRates;

    private LocalDate historicalStart;
    private LocalDate historicalEnd;
    private Map<LocalDate, Map<String, BigDecimal>> historicalRates;

    private Map<String, String> supportedCurrencies;
}
