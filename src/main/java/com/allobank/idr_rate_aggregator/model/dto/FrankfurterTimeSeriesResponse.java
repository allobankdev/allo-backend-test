package com.allobank.idr_rate_aggregator.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

/**
 * DTO for Frankfurter API time series response.
 * Maps to the response from time series endpoint.
 */
@Data
public class FrankfurterTimeSeriesResponse {
    
    /**
     * Amount used in the query (typically 1.0).
     */
    private Double amount;
    
    /**
     * Base currency.
     */
    private String base;
    
    /**
     * Start date of the series.
     */
    @JsonProperty("start_date")
    private String startDate;
    
    /**
     * End date of the series.
     */
    @JsonProperty("end_date")
    private String endDate;
    
    /**
     * Map of date to currency rates.
     * Structure: { "2024-01-01": { "USD": 0.064 }, ... }
     */
    private Map<String, Map<String, Double>> rates;
}
