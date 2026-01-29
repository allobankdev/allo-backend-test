package com.allobank.financeaggregator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Map;

public record LatestIdrRatesDto(
        BigDecimal amount,
        String base,
        String date,
        Map<String, BigDecimal> rates,
        @JsonProperty("USD_BuySpread_IDR") BigDecimal usdBuySpreadIdr
) {
}
