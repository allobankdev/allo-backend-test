package com.example.allobank.dto;

import java.math.BigDecimal;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RatesResponseDTO {

    private BigDecimal amount;
    private String base;
    private String date;

    private Map<String, BigDecimal> rates;
    private Map<String, SpreadDetailDTO> spread;

    @JsonProperty("USD_BuySpread_IDR")
    private BigDecimal usdBuySpreadIdr;
}


