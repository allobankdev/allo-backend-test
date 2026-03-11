package com.allobank.idrrates.dto;

import java.math.BigDecimal;

public record LatestRateItem(
        String currency,
        BigDecimal rate,
        BigDecimal usdBuySpreadIdr
) {
}
