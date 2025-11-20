package com.allo.idr.model;

import java.util.Map;

public class LatestRateResponse {
    private String base;
    private String date;
    private Map<String, Double> rates;
    private Double usdBuySpreadIdr;

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Map<String, Double> getRates() {
        return rates;
    }

    public void setRates(Map<String, Double> rates) {
        this.rates = rates;
    }

    public Double getUsdBuySpreadIdr() {
        return usdBuySpreadIdr;
    }

    public void setUsdBuySpreadIdr(Double usdBuySpreadIdr) {
        this.usdBuySpreadIdr = usdBuySpreadIdr;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
