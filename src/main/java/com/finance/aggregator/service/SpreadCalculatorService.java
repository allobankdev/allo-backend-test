package com.finance.aggregator.service;

import java.math.BigDecimal;

public interface SpreadCalculatorService {
    BigDecimal hitungSpread(BigDecimal usdRate);
    BigDecimal getSpreadFactor();
}