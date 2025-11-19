package com.example.feat.idr_rate_aggregator.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class LatestRatesResponse {
    private BigDecimal amount;
    private String base;
    private String date;
    private Map<String, BigDecimal> rates;
    private BigDecimal USDBuySpreadIDR;
}
