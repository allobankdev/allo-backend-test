package com.allobank.assignment.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LatestRatesResponse(
      BigDecimal amount,
      String base,
      LocalDate date,
      Map<String, BigDecimal> rates) {
}
