package com.allobank.frankfurter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Map;

public class LatestRatesResponse {
    private Map<String, BigDecimal> rates;
    private String base;
    private String date;

    public Map<String, BigDecimal> getRates() { return rates; }
    public void setRates(Map<String, BigDecimal> rates) { this.rates = rates; }

    public String getBase() { return base; }
    @JsonProperty("base")
    public void setBase(String base) { this.base = base; }

    public String getDate() { return date; }
    @JsonProperty("date")
    public void setDate(String date) { this.date = date; }
}