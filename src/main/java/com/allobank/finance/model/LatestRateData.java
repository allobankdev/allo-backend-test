package com.allobank.finance.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Map;


@Builder
public record LatestRateData(
        BigDecimal amount,
        String base,
        String date,
        Map<String, BigDecimal> rates,

        @JsonProperty("USD_BuySpread_IDR")
        BigDecimal usdBuySpreadIDR,
        BigDecimal spreadFactor
) implements FinanceData {

}
