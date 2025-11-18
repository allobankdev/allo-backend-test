package com.allo.backend.test.code.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class HistoricalRatesResponse {
    private Double amount;
    private String base;

    @JsonProperty("start_date")
    private String startDate;

    @JsonProperty("end_date")
    private String endDate;

    private Map<String, Map<String, Double>> rates;
}
