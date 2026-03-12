package com.allobank.idrrates.dto;

import java.math.BigDecimal;
import java.util.Map;

public record HistoricalRateItem(
        String date,
        Map<String, BigDecimal> rates
) {
}
