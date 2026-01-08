package org.allobanktest.dto;

public record LatestRatesItem(
        String base,
        String date,
        String currency,
        double rate,
        double usdBuySpreadIdr
) {
}
