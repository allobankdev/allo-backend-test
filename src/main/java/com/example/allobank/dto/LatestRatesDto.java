package com.example.allobank.dto;

import java.math.BigDecimal;
import java.util.Map;
import lombok.Data;

@Data
public class LatestRatesDto {
    private BigDecimal amount;
    private String base;
    private String date;
    private Map<String, BigDecimal> rates;
}