package com.personal.allo_backend_test.client.response;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
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
  @JsonProperty("start_date")
  private String startDate;
  @JsonProperty("end_date")
  private String endDate;
  private Map<String, Map<String, Double>> rates;
}

