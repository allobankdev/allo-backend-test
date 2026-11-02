package com.allobank.splitbill.api.dto.response;

import java.math.BigDecimal;

public record CurrencyBillSummaryResponse(
        String currency,
        BigDecimal youOwe,
        BigDecimal youAreOwed,
        BigDecimal netBalance
) {
}
