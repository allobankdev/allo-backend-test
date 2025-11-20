package com.nurmaya.allobank.idr_rate_aggregator.dto;

import java.util.Map;

import lombok.Data;

@Data
public class HistoricalRatesResponse {
    private double amount; 
    private String start_date; 
    private String end_date;
    private String base;  
    private Map<String, Map<String, Double>> rates;
}
