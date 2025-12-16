package com.allo.backend.model;

import java.util.Map;

public class HistoricalIdrUsdResponse {
    private String startDate;
    private String endDate;
    private String base;
    private Map<String, Map<String, Double>> rates;

    public HistoricalIdrUsdResponse() {}

    public HistoricalIdrUsdResponse(String startDate, String endDate, String base, Map<String, Map<String, Double>> rates) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.base = base;
        this.rates = rates;
    }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public String getBase() { return base; }
    public void setBase(String base) { this.base = base; }
    public Map<String, Map<String, Double>> getRates() { return rates; }
    public void setRates(Map<String, Map<String, Double>> rates) { this.rates = rates; }
}
