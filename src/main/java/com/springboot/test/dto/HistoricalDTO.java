package com.springboot.test.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Map;

public class HistoricalDTO {

    private double amount;
    private String base;

    @JsonProperty("start_date")
    private String startDate;
    @JsonProperty("end_date")
    private String endDate;
    private Map<String, Map<String, BigDecimal>> rates;

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

    public Map<String, Map<String, BigDecimal>> getRates() {
        return rates;
    }

    public void setRates(Map<String, Map<String, BigDecimal>> rates) {
        this.rates = rates;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
