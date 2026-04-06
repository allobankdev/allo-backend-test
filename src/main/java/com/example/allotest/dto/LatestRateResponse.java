package com.example.allotest.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LatestRateResponse {
    private String base;
    private String date;
    private Map<String, Double> rates;
    private Double usdBuySpreadIdr;
}
