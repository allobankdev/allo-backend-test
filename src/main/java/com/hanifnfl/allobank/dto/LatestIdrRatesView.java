package com.hanifnfl.allobank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Map;

public record LatestIdrRatesView(
        String base,
        String date,
        Map<String, Object> payload,
        @JsonProperty("USD_BuySpread_IDR")
        BigDecimal usdBuySpreadIdr,
        BigDecimal spreadFactor
) {}
