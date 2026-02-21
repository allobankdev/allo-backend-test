package com.allobank.finance.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class ExchangeRateResponse {

    private String baseCurrency;
    private String date;
    private Map<String, BigDecimal> rate;
}
