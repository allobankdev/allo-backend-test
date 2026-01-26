package com.example.AlloBank.dto;

public class LatestRateDto {

    private String currency;
    private double rate;
    private double usdBuySpreadIdr;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getUsdBuySpreadIdr() {
        return usdBuySpreadIdr;
    }

    public void setUsdBuySpreadIdr(double usdBuySpreadIdr) {
        this.usdBuySpreadIdr = usdBuySpreadIdr;
    }


}
