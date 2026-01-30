package com.example.allobank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class HistoricalResponse {
    private String base;

    @JsonProperty("start_date")
    private String startDate;

    @JsonProperty("end_date")
    private String endDate;

    // tanggal -> (mata uang -> nilai)
    private Map<String, Map<String, Double>> rates;
}
