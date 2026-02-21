package com.allobank.finance.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class LatestRateResponse {

    private String baseCurrency;
    private String date;
    private Map<String, BigDecimal> rates;
    private double usdSpreadIdr;
}
