package com.example.allo_bank.integration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class HistoricalIdrUsdDto {

    private BigDecimal amount;
    private String base;
    @JsonProperty("start_date")
    private String startDate;
    @JsonProperty("end_date")
    private String endDate;
    private Map<String, Map<String, BigDecimal>> rates;

}
