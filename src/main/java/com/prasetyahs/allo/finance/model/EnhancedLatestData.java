package com.prasetyahs.allo.finance.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public record EnhancedLatestData(
        String base,
        String date,
        Map<String, Double> rates,
        @JsonProperty("USD_BuySpread_IDR") Double usdBuySpreadIdr) {
}
