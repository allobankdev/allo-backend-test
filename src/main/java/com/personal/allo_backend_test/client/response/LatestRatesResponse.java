package com.personal.allo_backend_test.client.response;

import java.util.Map;

public record LatestRatesResponse (
  Double amount,
  String base,
  String date,
  Map<String, Double> rates
){}
