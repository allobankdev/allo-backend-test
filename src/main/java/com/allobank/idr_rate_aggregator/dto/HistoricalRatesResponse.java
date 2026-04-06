package com.allobank.idr_rate_aggregator.dto;

import lombok.Data;
import java.util.Map;

@Data
public class HistoricalRatesResponse {

    private String base;
    private String startDate;
    private String endDate;
    private Map<String, Map<String, Double>> rates;
}
