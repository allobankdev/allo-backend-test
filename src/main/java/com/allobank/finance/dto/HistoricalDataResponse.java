package com.allobank.finance.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HistoricalDataResponse {

    private String base;

    @JsonProperty("start_date")
    private String startDate;

    @JsonProperty("end_date")
    private String endDate;

    private Map<String, Map<String, Double>> rates;

    public HistoricalDataResponse() {
    }

    public HistoricalDataResponse(String base, String startDate, String endDate, Map<String, Map<String, Double>> rates) {
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
