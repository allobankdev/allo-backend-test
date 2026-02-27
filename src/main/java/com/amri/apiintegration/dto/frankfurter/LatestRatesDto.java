package com.amri.apiintegration.dto.frankfurter;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Map;

public record LatestRatesDto(
        String base,
        String date,
        Map<String, BigDecimal> rates,
        @JsonProperty("USD_BuySpread_IDR") BigDecimal usdBuySpreadIdr
) implements FinanceDataDto {
}
