package com.htc.allobank.strategy;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component("historical_idr_usd")
@AllArgsConstructor
public class HistoricalUsdFetcher implements DataFetcher {
    private final WebClient client;

    @Override
    public Mono<Object> fetch() {
        return client.get()
          .uri(uriBuilder -> uriBuilder
            .path("/2024-01-01..2024-01-05")
            .queryParam("from", "IDR")
            .queryParam("to", "USD")
            .build())
          .retrieve()
          .bodyToMono(Object.class);
    }
}
