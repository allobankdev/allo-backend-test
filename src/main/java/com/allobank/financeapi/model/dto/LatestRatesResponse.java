package com.allobank.financeapi.model.dto;

import java.util.Map;

public class LatestRatesResponse {
    private double amount;
    private String base;
    private String date;
    private Map<String, Double> rates;
    private Double USD_BuySpread_IDR;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
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

    public Double getUSD_BuySpread_IDR() {
        return USD_BuySpread_IDR;
    }

    public void setUSD_BuySpread_IDR(Double USD_BuySpread_IDR) {
        this.USD_BuySpread_IDR = USD_BuySpread_IDR;
    }
}
