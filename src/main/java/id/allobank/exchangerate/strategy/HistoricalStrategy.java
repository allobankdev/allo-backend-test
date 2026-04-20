package id.allobank.exchangerate.strategy;

import id.allobank.exchangerate.model.dto.HistoricalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class HistoricalStrategy implements IDRDataFetcher {

    private final WebClient webClient;

    @Override
    public String getType() {
        return "historical_idr_usd";
    }

    @Override
    public Object fetch() {
        return webClient.get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve()
                .bodyToMono(HistoricalResponse.class)
                .block();
    }
}