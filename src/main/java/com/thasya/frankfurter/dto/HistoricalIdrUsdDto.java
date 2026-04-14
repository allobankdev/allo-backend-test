package com.thasya.frankfurter.dto;

public class HistoricalIdrUsdDto {

    private String date;
    private double usdRate;

    public HistoricalIdrUsdDto(String date, double usdRate) {
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
