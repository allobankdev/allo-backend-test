package com.allobank.exercise.api.dto;

import com.allobank.exercise.api.integration.dto.ExchangeRateResponse;
import java.math.BigDecimal;
import java.util.Map;

public class ExchangeRate {
    private String base;
    private String date;
    private BigDecimal amount;
    private Map<String, BigDecimal> rates;
    private BigDecimal USDBuySpreadIDR;

    public void copyFrom(ExchangeRateResponse response, BigDecimal USDBuySpreadIDR){
        this.base = response.getBase();
        this.date = response.getDate();
        this.rates = response.getRates();
        this.amount = response.getAmount();
        this.USDBuySpreadIDR = USDBuySpreadIDR;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Map<String, BigDecimal> getRates() {
        return rates;
    }

    public void setRates(Map<String, BigDecimal> rates) {
        this.rates = rates;
    }

    public BigDecimal getUSDBuySpreadIDR() {
        return USDBuySpreadIDR;
    }

    public void setUSDBuySpreadIDR(BigDecimal USDBuySpreadIDR) {
        this.USDBuySpreadIDR = USDBuySpreadIDR;
    }
}
