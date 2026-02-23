package com.allobank.finance.idrrateaggregator.model.dto;

import java.util.Map;

public class HistoricalRatesResponse {

    private double amount;
    private String base;
    private String start_date;
    private String end_date;

    private Map<String, Map<String, Double>> rates;

    public Map<String, Map<String, Double>> getRates() {
        return rates;
    }

    public void setRates(Map<String, Map<String, Double>> rates) {
        this.rates = rates;
    }

    public String getBase() {
        return base;
    }
}
