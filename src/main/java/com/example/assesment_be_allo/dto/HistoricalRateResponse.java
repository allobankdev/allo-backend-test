package com.example.assesment_be_allo.dto;
import java.util.Map;

public class HistoricalRateResponse {

    private String date;
    private String base;
    private Map<String, Object> rates;

    public HistoricalRateResponse() {}

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Map<String, Object> getRates() {
        return rates;
    }

    public void setRates(Map<String, Object> rates) {
        this.rates = rates;
    }
}