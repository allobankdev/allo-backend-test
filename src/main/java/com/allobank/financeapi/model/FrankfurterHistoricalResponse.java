package com.allobank.financeapi.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FrankfurterHistoricalResponse {
    private String base;
    private LocalDate startDate;
    private LocalDate endDate;
    private Map<LocalDate, Map<String, BigDecimal>> rates;
}