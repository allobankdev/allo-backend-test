package com.allobank.idr_rate_aggregator.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

/**
 * DTO for Frankfurter API latest rates response.
 * Maps to the response from /latest endpoint.
 */
@Data
public class FrankfurterLatestResponse {
    
    /**
     * Exchange rates map: currency code -> rate value.
     */
    private Map<String, Double> rates;
    
    /**
     * Base currency (e.g., "IDR").
     */
    private String base;
    
    /**
     * Date of the rates (YYYY-MM-DD).
     */
    private String date;
}

