package com.frankfurter.aggregator.dto.external;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.Map;

public class LatestRatesResponse {
    @JsonProperty("base") private String base;
    @JsonProperty("date") private LocalDate date;
    @JsonProperty("rates") private Map<String, Double> rates;
    
    public String getBase() { return base; }
    public void setBase(String base) { this.base = base; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public Map<String, Double> getRates() { return rates; }
    public void setRates(Map<String, Double> rates) { this.rates = rates; }
}
