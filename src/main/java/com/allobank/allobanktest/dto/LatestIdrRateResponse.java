package com.allobank.allobanktest.dto;

import java.math.BigDecimal;
import java.util.Map;

public record LatestIdrRateResponse(
        BigDecimal amount,
        String base,
        String date,
        Map<String, BigDecimal> rates,
        BigDecimal usdBuySpreadIdr
) {
}