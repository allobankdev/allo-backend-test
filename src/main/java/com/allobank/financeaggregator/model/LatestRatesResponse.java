package com.allobank.financeaggregator.model;

import java.math.BigDecimal;
import java.util.Map;

public record LatestRatesResponse(
        BigDecimal amount,
        String base,
        String date,
        Map<String, BigDecimal> rates
) {
}
