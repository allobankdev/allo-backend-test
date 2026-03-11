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
public class FrankfurterLatestResponse {
    private String base;
    private LocalDate date;
    private Map<String, BigDecimal> rates;
}