package com.allobank.frankfurter_aggregator.dto;

import java.util.Map;

import lombok.Data;

@Data
public class LatestRatesResponse {
    private String base;
    private String date;
    private Map<String, Double> rates;
    private Double usdBuySpreadIdr;
}
