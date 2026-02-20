package com.allobank.aggregator.dto;

import java.math.BigDecimal;
import java.util.Map;

public record LatestRatesResponse(String base, String date, Map<String, BigDecimal> rates) {}
