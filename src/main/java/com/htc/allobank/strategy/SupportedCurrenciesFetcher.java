package com.htc.allobank.strategy;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component("supported_currencies")
public class SupportedCurrenciesFetcher implements IDRDataFetcher {
    private final WebClient client;
    public SupportedCurrenciesFetcher(WebClient client) { this.client = client; }

    @Override
    public Mono<Object> fetch() {
        return client.get()
          .uri("/currencies")
          .retrieve()
          .bodyToMono(Object.class);
    }
}
