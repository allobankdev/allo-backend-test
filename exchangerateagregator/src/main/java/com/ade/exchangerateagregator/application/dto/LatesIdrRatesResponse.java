package com.ade.exchangerateagregator.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LatesIdrRatesResponse implements FinanceBaseResponse {
    private BigDecimal amount;
    private String baseCurrency;
    private String date;
    private Map<String, BigDecimal> rates = new HashMap<>();
    private BigDecimal USD_BuySpread_IDR;
}
