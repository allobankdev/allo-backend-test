package com.allobank.backendtest.fetcher;

import com.allobank.backendtest.dto.HistoricalDto;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.*;

public class HistoricalIdrUsdFetcher implements IDRDataFetcher {
    private final WebClient client;

    public HistoricalIdrUsdFetcher(WebClient client) { this.client = client; }

    @Override public String resourceKey() { return "historical_idr_usd"; }

    @Override
    @SuppressWarnings("unchecked")
    public List<HistoricalDto> fetchSync() throws Exception {
        Map<String, Object> resp = client.get()
                .uri(uriBuilder -> uriBuilder.path("/2024-01-01..2024-01-05")
                        .queryParam("from","IDR")
                        .queryParam("to","USD")
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (resp == null || !resp.containsKey("rates")) {
            throw new IllegalStateException("Invalid response for historical rates");
        }

        Map<String, Map<String, Object>> rates =
                (Map<String, Map<String, Object>>) resp.get("rates");

        List<HistoricalDto> out = new ArrayList<>();

        rates.forEach((date, m) -> {
            Object usdRaw = m.get("USD");
            BigDecimal usd = usdRaw == null ? null : new BigDecimal(usdRaw.toString());
            out.add(new HistoricalDto(date, usd));
        });

        out.sort(Comparator.comparing(HistoricalDto::date));
        return out;
    }
}
