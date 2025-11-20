package com.hanifnfl.allobank.dto;

import java.math.BigDecimal;

public record HistoricalIdrUsdView(
        String date,
        BigDecimal usdRate
) {}
