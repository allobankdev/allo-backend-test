package org.imam.allo.dto;

import lombok.Data;

import java.util.Map;

@Data
public class HistoricalResponse {
    private double amount;
    private String base;
    private String start_date;
    private String end_date;

    private Map<String, Map<String, Double>> rates;
}
