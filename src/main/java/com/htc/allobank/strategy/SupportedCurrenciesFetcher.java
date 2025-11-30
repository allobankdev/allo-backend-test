package com.htc.allobank.strategy;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component("supported_currencies")
@AllArgsConstructor
public class SupportedCurrenciesFetcher implements IDRDataFetcher {
    private final WebClient client;

    @Override
    public Mono<Object> fetch() {
        return client.get()
          .uri("/currencies")
          .retrieve()
          .bodyToMono(Object.class);
    }
}
