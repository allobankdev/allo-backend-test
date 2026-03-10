package com.allo.bank.client.dto;

import java.util.Map;

public class FrankfurterHistoricalResponse {

    private Double amount;
    private String base;
    private Map<String, Map<String, Double>> rates;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Map<String, Map<String, Double>> getRates() {
        return rates;
    }

    public void setRates(Map<String, Map<String, Double>> rates) {
        this.rates = rates;
    }
}
