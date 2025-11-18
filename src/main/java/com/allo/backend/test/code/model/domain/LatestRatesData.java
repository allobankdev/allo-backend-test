package com.allo.backend.test.code.model.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class LatestRatesData {
    private Double amount;
    private String base;
    private String date;
    private Map<String, Double> rates;
    private Double usdBuySpreadIDR;
    private String spreadFactorNote;
}
