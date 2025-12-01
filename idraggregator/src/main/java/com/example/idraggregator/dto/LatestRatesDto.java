package com.example.idraggregator.dto;

import java.util.Map;

public class LatestRatesDto {
    private String base;
    private String date;
    private Map<String, Double> rates;

    // added custom field per requirement
    private Double USD_BuySpread_IDR;

    public LatestRatesDto() {}

    public LatestRatesDto(String base, String date, Map<String, Double> rates) {
        this.base = base;
        this.date = date;
        this.rates = rates;
    }

    // getters & setters

    public String getBase() { return base; }
    public void setBase(String base) { this.base = base; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public Map<String, Double> getRates() { return rates; }
    public void setRates(Map<String, Double> rates) { this.rates = rates; }

    public Double getUSD_BuySpread_IDR() { return USD_BuySpread_IDR; }
    public void setUSD_BuySpread_IDR(Double USD_BuySpread_IDR) { this.USD_BuySpread_IDR = USD_BuySpread_IDR; }
}
