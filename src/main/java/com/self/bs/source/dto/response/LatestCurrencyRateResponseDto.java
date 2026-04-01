package com.self.bs.source.dto.response;

import java.util.Map;

public class LatestCurrencyRateResponseDto {
    private String amount;
    private String base;
    private String date;
    private Map<String, String> rates;
    private Double USD_BuySpread_IDR;

    public LatestCurrencyRateResponseDto() {
    }

    public LatestCurrencyRateResponseDto(String amount, String base, String date, Map<String, String> rates, Double uSD_BuySpread_IDR) {
        this.amount = amount;
        this.base = base;
        this.date = date;
        this.rates = rates;
        this.USD_BuySpread_IDR = uSD_BuySpread_IDR;
    }

    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public String getBase() {
        return base;
    }
    public void setBase(String base) {
        this.base = base;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public Map<String, String> getRates() {
        return rates;
    }
    public void setRates(Map<String, String> rates) {
        this.rates = rates;
    }
    public Double getUSD_BuySpread_IDR() {
        return USD_BuySpread_IDR;
    }
    public void setUSD_BuySpread_IDR(Double uSD_BuySpread_IDR) {
        USD_BuySpread_IDR = uSD_BuySpread_IDR;
    }
    @Override
    public String toString() {
        return "LatestCurrencyRateResponseDto [amount=" + amount + ", base=" + base + ", date=" + date + ", rates="
                + rates + ", USD_BuySpread_IDR=" + USD_BuySpread_IDR + "]";
    }
}
