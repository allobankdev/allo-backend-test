package com.allobank.frankfurter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Map;

public class HistoricalRatesResponse {
    private Map<String, Map<String, BigDecimal>> rates;
    private String startDate;
    private String endDate;
    private String base;

    public Map<String, Map<String, BigDecimal>> getRates() { return rates; }
    public void setRates(Map<String, Map<String, BigDecimal>> rates) { this.rates = rates; }

    public String getStartDate() { return startDate; }
    @JsonProperty("start_date")
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    @JsonProperty("end_date")
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public String getBase() { return base; }
    public void setBase(String base) { this.base = base; }
}