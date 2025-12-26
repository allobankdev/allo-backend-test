package com.allobank.finance.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class HistoricalRateResponse {

    private String base;
    private Map<String, Map<String, Double>> rates;
}

