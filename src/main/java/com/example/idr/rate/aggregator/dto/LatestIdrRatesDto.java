package com.example.idr.rate.aggregator.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Setter
@Getter
public class LatestIdrRatesDto {
    private String base;
    private String date;
    private Map<String, Object> rates;
    private double usdRate;
    private BigDecimal usdBuySpreadIdr;
    private double spreadFactor;
}
