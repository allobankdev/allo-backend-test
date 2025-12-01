package com.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class LatestRatesResponseDto {
    private double amount;

    private String base;

    private String date;

    private Map<String, Double> rates;

    private String USDBuySpreadIDR;

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

    @JsonProperty("USD_BuySpread_IDR")
    public String getUSDBuySpreadIDR() {
        return USDBuySpreadIDR;
    }

    public void setUSDBuySpreadIDR(String USDBuySpreadIDR) {
        this.USDBuySpreadIDR = USDBuySpreadIDR;
    }
}
