package com.allobank.finnance.allobankfinance.dto.frankfurter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LatestRatesResponse {


    private String base;
    private String date;
    private Map<String, BigDecimal> rates;
    private String message;
}
