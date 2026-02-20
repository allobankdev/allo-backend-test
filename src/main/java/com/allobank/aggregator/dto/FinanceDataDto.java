package com.allobank.aggregator.dto;

import java.util.Map;

public record FinanceDataDto(String resourceType, Map<String, Object> payload) {}
