package com.aryaevan.allo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

/**
 * DTO for the Frankfurter API /latest endpoint response.
 */
public class LatestRatesResponse {
    
    @JsonProperty("base")
    private String base;
    
    @JsonProperty("date")
    private String date;
    
    @JsonProperty("rates")
    private Map<String, Double> rates;

    public LatestRatesResponse() {}

    public LatestRatesResponse(String base, String date, Map<String, Double> rates) {
        this.base = base;
        this.date = date;
        this.rates = rates;
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
}
