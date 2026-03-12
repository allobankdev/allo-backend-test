package com.allobank.idrrates.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record LatestRateItem(
        String currency,
        BigDecimal rate,
        @JsonProperty("USD_BuySpread_IDR") BigDecimal usdBuySpreadIdr
) {
}
