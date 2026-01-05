package com.allobank.allobanktest.dto;

import java.util.Map;

public record HistoricalRateResponse(
        double amount,
        String base,
        String start_date,
        String end_date,
        Map<String, Map<String, Double>> rates
) {}
