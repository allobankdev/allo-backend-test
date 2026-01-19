package com.example.allo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LatestRatesResponse {
    private String base;
    private Map<String, Double> rates;
    private Double usdBuySpreadIdr;
}

