package com.example.finance.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LatestRatesResponse {
	
	@JsonProperty("USD_BuySpread_IDR")
    private double usdBuySpreadIdr;
	
	private Map<String, Double> rates;

    public LatestRatesResponse(double usdBuySpreadIdr, Map<String, Double> rates) {
        this.usdBuySpreadIdr = usdBuySpreadIdr;
        this.rates = rates;
    }

    public double getUsdBuySpreadIdr() {
        return usdBuySpreadIdr;
    }

    public Map<String, Double> getRates() {
        return rates;
    }
}