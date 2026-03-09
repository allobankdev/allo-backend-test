package com.aryaevan.allo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

/**
 * DTO for the Frankfurter API time series endpoint response.
 */
public class HistoricalRatesResponse {
    
    @JsonProperty("base")
    private String base;
    
    @JsonProperty("start_date")
    private String startDate;
    
    @JsonProperty("end_date")
    private String endDate;
    
    @JsonProperty("rates")
    private Map<String, Map<String, Double>> rates;

    public HistoricalRatesResponse() {}

    public HistoricalRatesResponse(String base, String startDate, String endDate, Map<String, Map<String, Double>> rates) {
        this.base = base;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rates = rates;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
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

    public Map<String, Map<String, Double>> getRates() {
        return rates;
    }

    public void setRates(Map<String, Map<String, Double>> rates) {
        this.rates = rates;
    }
}
