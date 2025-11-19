package com.allobank.dto;

import java.time.LocalDate;
import java.util.Map;

public record LatestRatesResponse(
        String base,
        LocalDate date,
        Map<String, Double> rates
) {}