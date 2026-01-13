package com.example.assesment_be_allo.dto;
import java.util.Map;

public class LatestRatesResponse {

    private String base;
    private String date;
    private Map<String, Object> rates;
    private Double usdBuySpreadIdr;
    private Double spreadFactor;
    private String githubUsername;

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

    public Map<String, Object> getRates() {
        return rates;
    }

    public void setRates(Map<String, Object> rates) {
        this.rates = rates;
    }

    public Double getUsdBuySpreadIdr() {
        return usdBuySpreadIdr;
    }

    public void setUsdBuySpreadIdr(Double usdBuySpreadIdr) {
        this.usdBuySpreadIdr = usdBuySpreadIdr;
    }

    public Double getSpreadFactor() {
        return spreadFactor;
    }

    public void setSpreadFactor(Double spreadFactor) {
        this.spreadFactor = spreadFactor;
    }

    public String getGithubUsername() {
        return githubUsername;
    }

    public void setGithubUsername(String githubUsername) {
        this.githubUsername = githubUsername;
    }
}