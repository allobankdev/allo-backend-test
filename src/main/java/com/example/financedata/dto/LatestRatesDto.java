package com.example.financedata.dto;

import java.util.Map;

public class LatestRatesDto {
    private String base;
    private String date;
    private Map<String, Object> rates;
    private double usdRate;
    private double usdBuySpreadIdr;
    private double spreadFactor;

    // getters & setters omitted for brevity (add standard ones)
    // or use Lombok if preferred
    // ...
    
    public String getBase() { return base; }
    public void setBase(String base) { this.base = base; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public Map<String, Object> getRates() { return rates; }
    public void setRates(Map<String, Object> rates) { this.rates = rates; }
    public double getUsdRate() { return usdRate; }
    public void setUsdRate(double usdRate) { this.usdRate = usdRate; }
    public double getUsdBuySpreadIdr() { return usdBuySpreadIdr; }
    public void setUsdBuySpreadIdr(double usdBuySpreadIdr) { this.usdBuySpreadIdr = usdBuySpreadIdr; }
    public double getSpreadFactor() { return spreadFactor; }
    public void setSpreadFactor(double spreadFactor) { this.spreadFactor = spreadFactor; }
}
