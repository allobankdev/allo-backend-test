package com.hanifnfl.allobank.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FrankfurterLatestResponse(
        String base,
        LocalDate date,
        BigDecimal amount,
        Map<String, BigDecimal> rates
) {}
