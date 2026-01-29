package com.allobank.financeaggregator.strategy;

import com.allobank.financeaggregator.config.HistoricalProperties;
import com.allobank.financeaggregator.dto.HistoricalIdrUsdDto;
import com.allobank.financeaggregator.model.HistoricalRatesResponse;
import com.allobank.financeaggregator.service.FrankfurterClient;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component("historical_idr_usd")
public class HistoricalIdrUsdFetcher implements IDRDataFetcher {

    private final FrankfurterClient client;
    private final HistoricalProperties historicalProperties;

    public HistoricalIdrUsdFetcher(FrankfurterClient client, HistoricalProperties historicalProperties) {
        this.client = client;
        this.historicalProperties = historicalProperties;
    }

    @Override
    public HistoricalIdrUsdDto fetchData() {
        String rangeString = historicalProperties.resolveRangeString();
        String path = String.format("/%s?from=%s&to=%s",
                rangeString,
                historicalProperties.getFrom(),
                historicalProperties.getTo());

        HistoricalRatesResponse response = client.get(path, HistoricalRatesResponse.class);
        HistoricalProperties.Range range = historicalProperties.resolveRange();
        if (response == null || response.rates() == null || range == null) {
            return new HistoricalIdrUsdDto(
                    response == null ? null : response.amount(),
                    response == null ? null : response.base(),
                    response == null ? null : response.startDate(),
                    response == null ? null : response.endDate(),
                    response == null ? null : response.rates()
            );
        }

        Map<String, Map<String, BigDecimal>> filtered = new LinkedHashMap<>();
        for (Map.Entry<String, Map<String, BigDecimal>> entry : response.rates().entrySet()) {
            if (isWithinRange(entry.getKey(), range)) {
                filtered.put(entry.getKey(), entry.getValue());
            }
        }

        return new HistoricalIdrUsdDto(
                response.amount(),
                response.base(),
                range.start().toString(),
                range.end().toString(),
                Map.copyOf(filtered)
        );
    }

    private boolean isWithinRange(String dateValue, HistoricalProperties.Range range) {
        try {
            java.time.LocalDate date = java.time.LocalDate.parse(dateValue);
            return !date.isBefore(range.start()) && !date.isAfter(range.end());
        } catch (Exception ex) {
            return false;
        }
    }
}
