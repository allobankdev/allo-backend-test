package com.sdewa.IdrRateAggregator.dtoes;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LatestIdrRatesWithSpreadRecord {
    private Double amount;
    private String base;
    private String date;
    private String countryCode;
    private BigDecimal rates;
    private Double usdBuySpreadIdr;
}
