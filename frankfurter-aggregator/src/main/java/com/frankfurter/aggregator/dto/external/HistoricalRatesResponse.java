package com.frankfurter.aggregator.dto.external;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.Map;

public class HistoricalRatesResponse {
    @JsonProperty("base") private String base;
    @JsonProperty("start_date") private LocalDate startDate;
    @JsonProperty("end_date") private LocalDate endDate;
    @JsonProperty("rates") private Map<String, Map<String, Double>> rates;
    
    public String getBase() { return base; }
    public void setBase(String base) { this.base = base; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public Map<String, Map<String, Double>> getRates() { return rates; }
    public void setRates(Map<String, Map<String, Double>> rates) { this.rates = rates; }
}
