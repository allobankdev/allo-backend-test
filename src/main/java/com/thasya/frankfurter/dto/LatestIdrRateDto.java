package com.thasya.frankfurter.dto;

public class LatestIdrRateDto {

    private String date;
    private String base;
    private double usdRate;            // Rate USD dari Frankfurter (IDR → USD)
    private double usdBuySpreadIdr;    // hasil formula (1 / usdRate) * (1 + spread)

    public LatestIdrRateDto(String date, String base, double usdRate, double usdBuySpreadIdr) {
        this.date = date;
        this.base = base;
        this.usdRate = usdRate;
        this.usdBuySpreadIdr = usdBuySpreadIdr;
    }

    public String getDate() {
        return date;
    }

    public String getBase() {
        return base;
    }

    public double getUsdRate() {
        return usdRate;
    }

    public double getUsdBuySpreadIdr() {
        return usdBuySpreadIdr;
    }
}
