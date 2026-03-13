package com.allobank.backend.test.model;

import lombok.Data;
import java.util.Map;

@Data
public class LatestRatesResponse {
    private int amount;
    private String base;
    private String date;
    private Map<String, Double> rates;
    private Double usdBuySpreadIdr;
}