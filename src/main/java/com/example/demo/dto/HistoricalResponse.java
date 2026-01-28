package com.example.demo.dto;

import lombok.Data;
import java.util.Map;

@Data
public class HistoricalResponse {

    private double amount;
    private String base;

    private Map<String, Map<String, Double>> rates;
}
