package com.personal.allo_backend_test.client.response;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public record HistoricalRatesResponse(
  Double amount,
  String base,
  @JsonProperty("start_date")
  String startDate,
  @JsonProperty("end_date")
  String endDate,
  Map<String, Map<String, Double>> rates
){}
