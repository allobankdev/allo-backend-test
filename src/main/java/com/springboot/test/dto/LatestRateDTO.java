package com.springboot.test.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Map;

public class LatestRateDTO {

    private double amount;
    private String base;
    private String date;
    private Map<String, BigDecimal> rates;

    @JsonProperty("USD_BuySpread_IDR")
    private BigDecimal USDBuySpreadIDR;

    public LatestRateDTO(double amount, String base, String date, Map<String, BigDecimal> rates, BigDecimal USDBuySpreadIDR) {
        this.amount = amount;
        this.base = base;
        this.date = date;
        this.rates = rates;
        this.USDBuySpreadIDR = USDBuySpreadIDR;
    }

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

    public Map<String, BigDecimal> getRates() {
        return rates;
    }

    public void setRates(Map<String, BigDecimal> rates) {
        this.rates = rates;
    }

    public void setUSDBuySpreadIDR(BigDecimal USDBuySpreadIDR) {
        this.USDBuySpreadIDR = USDBuySpreadIDR;
    }
}
