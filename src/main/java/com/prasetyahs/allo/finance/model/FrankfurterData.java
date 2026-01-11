package com.prasetyahs.allo.finance.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;

// Generic response for API
@JsonInclude(JsonInclude.Include.NON_NULL)
public record FrankfurterData(
        Double amount,
        String base,
        String date,
        Map<String, Double> rates,
        // For historical which has Map<String, Map<String, Double>> rates
        Map<String, Map<String, Double>> historicalRates) {
}
