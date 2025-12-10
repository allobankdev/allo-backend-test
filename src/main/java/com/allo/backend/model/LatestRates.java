package com.allo.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LatestRates {

    private String base;
    private String date;
    private Map<String, Double> rates;

    @JsonProperty("USD_BuySpread_IDR")
    private Double usdBuySpreadIdr;
}
