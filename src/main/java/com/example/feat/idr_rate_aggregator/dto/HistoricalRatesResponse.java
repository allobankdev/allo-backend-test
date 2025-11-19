package com.example.feat.idr_rate_aggregator.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class HistoricalRatesResponse {
    private BigDecimal amount;
    private String base;
    private String start_date;
    private String end_date;
    private Map<String, Map<String, BigDecimal>> rates;
}
