package com.prasetyahs.allo.finance.strategy;

import com.prasetyahs.allo.finance.model.HistoricalRateEntry;
import com.prasetyahs.allo.finance.model.HistoricalResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("historical_idr_usd")
public class HistoricalIDRFetcher implements IDRDataFetcher {

    @Override
    public String getResourceType() {
        return "historical_idr_usd";
    }

    @Override
    public Object fetchAndProcess(WebClient client) {
        HistoricalResponse response = client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/2024-01-01..2024-01-05")
                        .queryParam("from", "IDR")
                        .queryParam("to", "USD")
                        .build())
                .retrieve()
                .bodyToMono(HistoricalResponse.class)
                .block();

        if (response == null || response.rates() == null) {
            return new ArrayList<>();
        }

        // Transform Map<Date, Map<Currency, Rate>> to List<HistoricalRateEntry>
        return response.rates().entrySet().stream()
                .map(entry -> {
                    String date = entry.getKey();
                    Double rate = entry.getValue().get("USD");
                    return new HistoricalRateEntry(date, rate);
                })
                .sorted((a, b) -> a.date().compareTo(b.date()))
                .collect(Collectors.toList());
    }
}
