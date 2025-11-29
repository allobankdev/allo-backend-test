package com.allobank.backendtest.dto;

import java.math.BigDecimal;

public record LatestRateDto(String currency, BigDecimal rateUsdWhenBaseIdr, BigDecimal usdBuySpreadIdr) {}
