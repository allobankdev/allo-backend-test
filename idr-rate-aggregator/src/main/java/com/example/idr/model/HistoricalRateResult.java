package com.example.idr.model;

public class HistoricalRateResult {

    private String date;
    private double usdRate;

    public HistoricalRateResult(String date, double usdRate) {
        this.date = date;
        this.usdRate = usdRate;
    }

    public String getDate() {
        return date;
    }

    public double getUsdRate() {
        return usdRate;
    }
}
