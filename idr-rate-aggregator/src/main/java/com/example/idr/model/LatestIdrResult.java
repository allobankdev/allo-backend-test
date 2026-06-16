package com.example.idr.model;

import java.util.Map;

public class LatestIdrResult {

    private String date;
    private Map<String, Double> rates;
    private double usdBuySpreadIdr;

    public LatestIdrResult(String date,
                           Map<String, Double> rates,
                           double usdBuySpreadIdr) {
        this.date = date;
        this.rates = rates;
        this.usdBuySpreadIdr = usdBuySpreadIdr;
    }

    public String getDate() {
        return date;
    }

    public Map<String, Double> getRates() {
        return rates;
    }

    public double getUsdBuySpreadIdr() {
        return usdBuySpreadIdr;
    }
}
