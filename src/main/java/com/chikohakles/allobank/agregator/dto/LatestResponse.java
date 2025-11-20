package com.chikohakles.allobank.agregator.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Data
public class LatestResponse {
    private BigDecimal amount;
    private String baseCurrency;
    private Date date;
    Map<String, BigDecimal> rates;
}
