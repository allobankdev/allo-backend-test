package com.allo.backend.test.code.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class LatestRatesResponse {
    private Double amount;
    private String base;
    private String date;
    private Map<String, Double> rates;
}
