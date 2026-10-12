package com.allobank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricalRatesResponse {
    private String base;
    private String startDate;
    private String endDate;
    private Map<String, Map<String, BigDecimal>> rates;
}
