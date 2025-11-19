package com.allobank.exercise.api.dto;

import com.allobank.exercise.api.integration.dto.ExchangeHistoryResponse;

import java.util.List;
import java.util.Map;

public class ExchangeHistory {
    private String base;
    private String startDate;
    private String endDate;
    private Map<String, Map<String, Double>> rates;

    public void copyFrom(ExchangeHistoryResponse response){
        this.base = response.getBase();
        this.startDate = response.getStart_date();
        this.endDate = response.getEnd_date();
        this.rates = response.getRates();
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
