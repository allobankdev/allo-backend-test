package com.allobank.frankfurter_aggregator.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FrankfurterResponse {
    private Double amount;
    private String base;
    private String date;
    
    @JsonProperty("start_date")
    private String startDate;
    
    @JsonProperty("end_date")
    private String endDate;
    
    private Map<String, Double> rates;
    
    @Data
    public static class CurrenciesResponse {
        private Map<String, String> currencies;
    }
}
