package com.finance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LatestIdrRatesResponse {
    private Double amount;
    private String base;
    private String date;
    private Map<String, Double> rate;
    private Double USD_BuySpread_IDR;
}
