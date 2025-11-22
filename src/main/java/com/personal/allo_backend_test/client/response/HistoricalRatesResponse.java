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
public class HistoricalRatesResponse {
  private Double amount;
  private String base;
  private String startDate;
  private String endDate;
  private Map<String, Map<String, Double>> rates;
}

