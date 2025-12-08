package com.tes.allo.dto;

import java.util.Map;

public class LatestRatesDto {
    private String base;
    private String date;
    private Map<String, Double> rates;
    private double usdBuySpreadIdr;
    private double spreadFactor;

    public LatestRatesDto() {}
    public LatestRatesDto(String base, String date, Map<String, Double> rates, double usdBuySpreadIdr, double spreadFactor) {
        this.base = base; this.date = date; this.rates = rates;
        this.usdBuySpreadIdr = usdBuySpreadIdr; this.spreadFactor = spreadFactor;
    }
    // getters & setters...
    public String getBase(){return base;}
    public void setBase(String base){this.base = base;}
    public String getDate(){return date;}
    public void setDate(String date){this.date = date;}
    public Map<String, Double> getRates(){return rates;}
    public void setRates(Map<String, Double> rates){this.rates = rates;}
    public double getUsdBuySpreadIdr(){return usdBuySpreadIdr;}
    public void setUsdBuySpreadIdr(double usdBuySpreadIdr){this.usdBuySpreadIdr = usdBuySpreadIdr;}
    public double getSpreadFactor(){return spreadFactor;}
    public void setSpreadFactor(double spreadFactor){this.spreadFactor = spreadFactor;}
}
