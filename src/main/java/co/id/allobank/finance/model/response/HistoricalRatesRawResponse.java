package co.id.allobank.finance.model.response;

import io.soabase.recordbuilder.core.RecordBuilder;

import java.util.Map;

@RecordBuilder
public record HistoricalRatesRawResponse(
        double amount,
        String base,
        Map<String, Map<String, Double>> rates
) {
}
