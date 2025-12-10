package com.allo.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoricalRates {

    private String base;
    private String startDate;
    private String endDate;
    private Map<String, Map<String, Double>> rates;
}
