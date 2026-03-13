package com.allobank.backend.test.model;

import lombok.Data;
import java.util.Map;

@Data
public class HistoricalRatesResponse {
    private int amount;
    private String base;
    private Map<String, Map<String, Double>> rates;
}