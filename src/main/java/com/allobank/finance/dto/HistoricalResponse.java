package com.allobank.finance.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class HistoricalResponse {

    private String baseCurrency;
    private Map<String, Map<String, BigDecimal>> rate;
}
