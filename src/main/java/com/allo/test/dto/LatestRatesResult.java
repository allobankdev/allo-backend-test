package com.allo.test.dto;

public class LatestRatesResult {

    private double usdRate;
    private double spreadFactor;
    private double usdBuySpreadIdr;

    public LatestRatesResult(double usdRate, double spreadFactor, double usdBuySpreadIdr) {
        this.usdRate = usdRate;
        this.spreadFactor = spreadFactor;
        this.usdBuySpreadIdr = usdBuySpreadIdr;
    }

    public double getUsdRate() {
        return usdRate;
    }

    public double getSpreadFactor() {
        return spreadFactor;
    }

    public double getUsdBuySpreadIdr() {
        return usdBuySpreadIdr;
    }
}