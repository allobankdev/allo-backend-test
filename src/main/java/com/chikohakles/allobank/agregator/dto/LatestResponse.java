package com.chikohakles.allobank.agregator.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Data
public class LatestResponse {
    private BigDecimal USD_BuySpread_IDR;
    private BigDecimal amount;
    private String base;
    private Date date;
    Map<String, BigDecimal> rates;
}
