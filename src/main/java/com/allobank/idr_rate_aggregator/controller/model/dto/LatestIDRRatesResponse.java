package com.allobank.idr_rate_aggregator.controller.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Enhanced response for latest IDR rates with spread calculation.
 * This is the enriched response returned to API consumers.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LatestIDRRatesResponse {
    
    /**
     * Exchange rates map: currency code -> rate value.
     */
    private Map<String, Double> rates;
    
    /**
     * Base currency (always "IDR" for this service).
     */
    private String base;
    
    /**
     * Date of the rates.
     */
    private String date;
    
    /**
     * Calculated USD buy spread rate.
     * This is the rate when buying USD with IDR including spread.
     */
    @JsonProperty("USD_BuySpread_IDR")
    private Double usdBuySpreadIdr;
    
    /**
     * GitHub username used for spread calculation.
     */
    @JsonProperty("github_username")
    private String githubUsername;
}
