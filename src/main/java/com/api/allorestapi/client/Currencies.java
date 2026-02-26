package com.api.allorestapi.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class Currencies implements Frankfurter<Map<String, String>> {

    private final WebClient frankfurterWebClient;

    @Override
    @SuppressWarnings("unchecked")
    public Mono<Map<String, String>> fetch() {
        log.debug("Fetching supported currencies from Frankfurter API");
        return frankfurterWebClient
                .get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(Map.class)
                .cast(Map.class)
                .map(m -> (Map<String, String>) m)
                .doOnSuccess(m -> log.debug("Received {} supported currencies", m.size()))
                .doOnError(e -> log.error("Failed to fetch currencies", e));
    }
}
