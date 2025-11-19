package com.athallah.finance.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class LatestRatesRawDto {
    private int amount;
    private String base;
    private String date;
    private Map<String, BigDecimal> rates;
}
