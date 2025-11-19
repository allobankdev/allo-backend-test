package com.athallah.finance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LatestRatesResponseDto {
    private int amount;
    private String base;
    private String date;
    private Map<String, BigDecimal> rates;

    private BigDecimal usdBuySpreadIdr;
}
