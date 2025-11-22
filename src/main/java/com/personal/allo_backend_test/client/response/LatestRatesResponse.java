package com.personal.allo_backend_test.client.response;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LatestRatesResponse {
  private Double amount;
  private String base;
  private String date;
  private Map<String, Double> rates;
}

