package com.nurmaya.allobank.idr_rate_aggregator.dto;

import java.util.Map;

import lombok.Data;

@Data
public class LatestRatesResponse {
    private double amount;   
    private String base;   
    private String date; 
    private Map<String, Double> rates; 

    private Double usdBuySpreadIdr; 
}
