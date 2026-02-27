package com.allobank.finance.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class LatestIdrRatesDto {

    private int amount;
    private String base;
    private String date;
    private Map<String, Double> rates;
    @JsonProperty("USD_BuySpread_IDR")
    private Double usdBuySpreadIdr;
}