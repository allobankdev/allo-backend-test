package com.interview.backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSeriesResponse {
    private String base;
    private String startDate;
    private String endDate;
    private Map<String, Map<String, Double>> rates;
}
