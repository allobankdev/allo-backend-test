package com.finance.aggregator.dto;

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
public class LatestRatesDTO {
    private String base;
    private String date;
    private Map<String, BigDecimal> rates;
    private BigDecimal usdBuySpreadIdr;
    private String resourceType;
}