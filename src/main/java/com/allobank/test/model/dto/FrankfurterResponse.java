package com.allobank.test.model.dto;

import lombok.Data;
import java.util.Map;

@Data
public class FrankfurterResponse {
    private Double amount;
    private String base;
    private String date;
    private Map<String, Double> rates;
}