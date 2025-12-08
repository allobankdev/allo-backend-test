package com.tes.allo.dto;

import java.util.Map;

public class HistoricalDto {
    private String fromDate;
    private String toDate;
    private String base;
    private String target;
    private Map<String, Map<String, Double>> rates;

    public HistoricalDto() {}
    public HistoricalDto(String fromDate, String toDate, String base, String target, Map<String, Map<String, Double>> rates) {
        this.fromDate = fromDate; this.toDate = toDate; this.base = base; this.target = target; this.rates = rates;
    }
    public String getFromDate(){return fromDate;}
    public void setFromDate(String fromDate){this.fromDate = fromDate;}
    public String getToDate(){return toDate;}
    public void setToDate(String toDate){this.toDate = toDate;}
    public String getBase(){return base;}
    public void setBase(String base){this.base = base;}
    public String getTarget(){return target;}
    public void setTarget(String target){this.target = target;}
    public Map<String, Map<String, Double>> getRates(){return rates;}
    public void setRates(Map<String, Map<String, Double>> rates){this.rates = rates;}
}
