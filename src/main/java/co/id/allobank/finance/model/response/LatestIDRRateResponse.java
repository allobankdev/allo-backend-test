package co.id.allobank.finance.model.response;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record LatestIDRRateResponse(
        String currency,
        double rate,
        double usdBuySpreadIdr
) {
}
