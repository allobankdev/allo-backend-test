package com.allobank.financeaggregator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Map;

public record HistoricalIdrUsdDto(
        BigDecimal amount,
        String base,
        @JsonProperty("start_date") String startDate,
        @JsonProperty("end_date") String endDate,
        Map<String, Map<String, BigDecimal>> rates
) {
}
