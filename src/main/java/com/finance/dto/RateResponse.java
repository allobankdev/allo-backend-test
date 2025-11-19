package com.finance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RateResponse {
    private Double amount;
    private String base;
    private String date;
    private Map<String, Double> rates;
}

