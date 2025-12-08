package com.tes.allo.fetcher;

import com.tes.allo.dto.HistoricalDto;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

public class HistoricalIdrUsdFetcher implements IDRDataFetcher {

    private final WebClient webClient;

    public HistoricalIdrUsdFetcher(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Object fetch() {
        String from = "2024-01-01";
        String to = "2024-01-05";

        Map<String, Object> resp = webClient.get()
                .uri("/{from}..{to}?from=IDR&to=USD", from, to)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (resp == null) {
            throw new RuntimeException("Empty historical response");
        }

        String base = (String) resp.get("base");
        Map<String, Map<String, Double>> rates =
                (Map<String, Map<String, Double>>) resp.get("rates");

        return new HistoricalDto(from, to, base, "USD", rates);
    }

    @Override
    public String key() {
        return "historical_idr_usd";
    }
}
