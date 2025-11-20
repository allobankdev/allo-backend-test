package com.example.allow.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UsdBuySpreadWrapper(
        LatestRatesResponse original,
        double USD_BuySpread_IDR,
        double appliedSpreadFactor,
        String githubUsername
) {
    public UsdBuySpreadWrapper(LatestRatesResponse original, double USD_BuySpread_IDR, double appliedSpreadFactor) {
        this(original, USD_BuySpread_IDR, appliedSpreadFactor, "hafizs08");
    }
}
