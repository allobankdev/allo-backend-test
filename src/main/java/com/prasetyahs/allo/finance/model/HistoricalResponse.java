package com.prasetyahs.allo.finance.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public record HistoricalResponse(
        String amount,
        String base, // sometimes base is returned
        @JsonProperty("start_date") String startDate,
        @JsonProperty("end_date") String endDate,
        Map<String, Map<String, Double>> rates) {
}
