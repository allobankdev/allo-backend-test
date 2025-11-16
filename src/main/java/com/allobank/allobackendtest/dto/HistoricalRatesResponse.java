package com.allobank.allobackendtest.dto;

import java.math.BigDecimal;
import java.util.Map;

public record HistoricalRatesResponse(
        String resourceType,
        Map<String, Map<String, BigDecimal>> rates
) {}
