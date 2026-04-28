package com.allobankdev.allo_idr_rate_aggregator.model.dto;

import lombok.Data;

import java.util.Map;

@Data
public class FrankfurterLatestResponse {
    private String base;
    private Map<String, Double> rates;
}
