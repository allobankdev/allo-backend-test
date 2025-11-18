package com.allo.backend.test.code.model.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class HistoricalRatesData {
    private Double amount;
    private String base;
    private String startDate;
    private String endDate;
    private Map<String, Map<String, Double>> rates;
}
