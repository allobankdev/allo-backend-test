package com.example.idr.client.dto;

import java.util.Map;

public class HistoricalRatesResponse {

    private Map<String, Map<String, Double>> rates;

    public Map<String, Map<String, Double>> getRates() {
        return rates;
    }

    public void setRates(Map<String, Map<String, Double>> rates) {
        this.rates = rates;
    }
}
