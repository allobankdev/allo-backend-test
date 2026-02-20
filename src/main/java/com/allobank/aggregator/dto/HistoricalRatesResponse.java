package com.allobank.aggregator.dto;

import java.util.Map;

public record HistoricalRatesResponse(Map<String, Map<String, java.math.BigDecimal>> rates) {}
