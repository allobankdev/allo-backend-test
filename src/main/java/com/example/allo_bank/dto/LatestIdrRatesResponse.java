package com.example.allo_bank.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class LatestIdrRatesResponse {

    private String base;
    private String date;
    private Map<String, BigDecimal> rates;
    private BigDecimal usdBuySpreadIdr;

}
