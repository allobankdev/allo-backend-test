package com.example.allo_bank.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class HistoricalIdrUsdResponse {

    private BigDecimal amount;
    private String base;
    private String startDate;
    private String endDate;
    private Map<String, Map<String, BigDecimal>> rates;

}
