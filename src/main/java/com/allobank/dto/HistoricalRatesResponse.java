package com.allobank.dto;

import java.util.Map;

public record HistoricalRatesResponse(
        Map<String, Map<String, Double>> rates
) {}