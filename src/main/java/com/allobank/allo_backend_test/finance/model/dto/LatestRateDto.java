package com.allobank.allo_backend_test.finance.model.dto;

import java.time.LocalDate;
import java.util.Map;

public record LatestRateDto(
        Double amount,
        String base,
        LocalDate date,
        Map<String, Double> rates
) {}