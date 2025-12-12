package com.allobank.finance.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LatestRatesResponse {

    private String base;
    private String date;
    private Map<String, Double> rates;

    @JsonProperty("USD_BuySpread_IDR")
    private Double usdBuySpreadIdr;

    public LatestRatesResponse() {
    }

    public LatestRatesResponse(String base, String date, Map<String, Double> rates, Double usdBuySpreadIdr) {
        this.base = base;
        this.date = date;
        this.rates = rates;
        this.usdBuySpreadIdr = usdBuySpreadIdr;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
}
