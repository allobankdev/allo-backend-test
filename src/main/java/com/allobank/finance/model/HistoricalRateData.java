package com.allobank.finance.model;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.Map;

@Builder(toBuilder = true)
public record HistoricalRateData(
        BigDecimal amount,
        String base,
        String startDate,
        String endDate,
        Map<String, Map<String, BigDecimal>> rates
) implements FinanceData {
}
