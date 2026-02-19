package co.id.allobank.finance.model.response;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record SupportedCurrencyResponse(
        String code,
        String name
) {
}
