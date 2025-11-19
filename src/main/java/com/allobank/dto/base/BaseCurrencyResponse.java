package com.allobank.dto.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Map;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseCurrencyResponse {
    protected Double amount;
    protected String base;
    protected Map<String, BigDecimal> rates;
}
