package com.finance.aggregator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoricalRatesDTO {
    private String base;
    private Map<String, Map<String, Double>> rates;
    private String resourceType;
}