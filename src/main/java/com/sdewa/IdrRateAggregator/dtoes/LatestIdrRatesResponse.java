package com.sdewa.IdrRateAggregator.dtoes;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LatestIdrRatesResponse {
    private double amount;
    private String base;
    private String date;
    private Map<String, Double> rates;
}
