package com.allo.idr.model;

public class HistoricalRatesResponse {
    private String date;
    private Double usdRate;

    public HistoricalRatesResponse(String date, Double usdRate) {
        this.date = date;
        this.usdRate = usdRate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getUsdRate() {
        return usdRate;
    }

    public void setUsdRate(Double usdRate) {
        this.usdRate = usdRate;
    }
}
