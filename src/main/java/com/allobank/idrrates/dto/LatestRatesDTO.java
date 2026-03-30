package com.allobank.idrrates.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LatestRatesDTO {
    private Double amount;
    private String base;
    private String date;
    private Map<String, Double> rates;

    @JsonProperty("USD_BuySpread_IDR")
    private Double usdBuySpreadIdr;
}
