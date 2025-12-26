package com.allobank.finance.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class LatestRateResponse {

    private String base;
    private String date;
    private Map<String, Double> rates;

    // custom fields
    private Double usdBuySpreadIdr;
    private Double spreadFactor;
}

