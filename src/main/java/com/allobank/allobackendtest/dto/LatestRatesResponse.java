package com.allobank.allobackendtest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public record LatestRatesResponse(
        String resourceType,
        String base,
        LocalDate date,
        Map<String, BigDecimal> rates,
        BigDecimal spreadFactor,
        BigDecimal usdBuySpreadIdr
) {}
