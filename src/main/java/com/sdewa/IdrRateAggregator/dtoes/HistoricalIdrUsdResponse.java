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
public class HistoricalIdrUsdResponse {
        private double amount;
    private String base;
    private String startDate;
    private String endDate;
    private Map<String, Map<String, Double>> rates; 
}
