package com.allo.backendtest.dto;

import java.util.Map;

public record HistoricalResponse(
        String base,
        Map<String, Map<String, Double>> rates
) {}
