package com.idr_rate_aggregator_2.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LatestRatesResponse {
    private String base;
    private LocalDate date;
    private Map<String, BigDecimal> rates;
    private BigDecimal USD_BuySpread_IDR;
    private String resourceType;
}