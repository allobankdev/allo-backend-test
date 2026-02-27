package com.allobank.finance.integration.frankfurter.dto;

import io.soabase.recordbuilder.core.RecordBuilder;

import java.util.Map;

@RecordBuilder
public record LatestRatesRawResponse(
        double amount,
        String base,
        String date,
        Map<String, Double> rates
) {
}
