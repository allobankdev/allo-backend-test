package com.allo.backend.model;

import java.util.Map;

public class LatestIdrRatesResponse {
    private String base;
    private String date;
    private Map<String, Double> rates;
    private Double usdBuySpreadIdr;

    public LatestIdrRatesResponse() {}

    public LatestIdrRatesResponse(String base, String date, Map<String, Double> rates, Double usdBuySpreadIdr) {
        this.base = base;
        this.date = date;
        this.rates = rates;
        this.usdBuySpreadIdr = usdBuySpreadIdr;
    }

    public String getBase() { return base; }
    public void setBase(String base) { this.base = base; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public Map<String, Double> getRates() { return rates; }
    public void setRates(Map<String, Double> rates) { this.rates = rates; }
    public Double getUsdBuySpreadIdr() { return usdBuySpreadIdr; }
    public void setUsdBuySpreadIdr(Double usdBuySpreadIdr) { this.usdBuySpreadIdr = usdBuySpreadIdr; }
}
