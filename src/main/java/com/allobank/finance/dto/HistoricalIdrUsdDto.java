package com.allobank.finance.dto;

import lombok.Data;

import java.util.Map;

@Data
public class HistoricalIdrUsdDto {

    private int amount;
    private String base;
    private Map<String, Map<String, Double>> rates;
    private String startDate;
    private String endDate;
}
