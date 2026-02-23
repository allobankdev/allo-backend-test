package com.allo.backendtest.dto;

import java.util.Map;

public record LatestRatesResponse(
        String base,
        String date,
        Map<String, Double> rates
) {}
