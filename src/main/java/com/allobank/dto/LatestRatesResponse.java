package com.allobank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LatestRatesResponse {
    private BigDecimal amount;
    private String base;
    private LocalDate date;
    private Map<String, BigDecimal> rates;
    
    @JsonProperty("USD_BuySpread_IDR")
    private BigDecimal usdBuySpreadIdr;
}

