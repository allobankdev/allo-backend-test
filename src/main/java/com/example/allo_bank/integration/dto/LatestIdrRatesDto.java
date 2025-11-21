package com.example.allo_bank.integration.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class LatestIdrRatesDto {

    private String base;
    private String date;
    private Map<String, BigDecimal> rates;

}
