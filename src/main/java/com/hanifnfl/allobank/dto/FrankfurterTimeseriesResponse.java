package com.hanifnfl.allobank.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FrankfurterTimeseriesResponse(
        String base,
        String start_date,
        String end_date,
        Map<String, Map<String, BigDecimal>> rates
) {}
