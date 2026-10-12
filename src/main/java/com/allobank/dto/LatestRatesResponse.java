package com.allobank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LatestRatesResponse {
    private String base;
    private String date;
    private Map<String, BigDecimal> rates;
    
    @JsonProperty("USD_BuySpread_IDR")
    private BigDecimal usdBuySpreadIDR;
}
