package com.athallah.finance.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class HistoricalRatesRawDto {
    private int amount;
    private String base;

    @JsonProperty("start_date")
    private String startDate;

    @JsonProperty("end_date")
    private String endDate;

    private Map<String, Map<String, BigDecimal>> rates;
}