package com.example.finance.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HistoricalRatesResponse {

    private double amount;
    private String base;

    @JsonProperty("start_date")
    private String startDate;

    @JsonProperty("end_date")
    private String endDate;

    private Map<String, Map<String, Double>> rates;

    public HistoricalRatesResponse() {
    }

    public double getAmount() {
        return amount;
    }

    public String getBase() {
        return base;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public Map<String, Map<String, Double>> getRates() {
        return rates;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setRates(Map<String, Map<String, Double>> rates) {
        this.rates = rates;
    }
}