package com.api.allorestapi.client;

import com.api.allorestapi.model.LatestRatesResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class LatestRates implements Frankfurter<LatestRatesResponse> {

    private final WebClient frankfurterWebClient;

    @Override
    public Mono<LatestRatesResponse> fetch() {
        log.debug("Fetching latest IDR rates from Frankfurter API");
        return frankfurterWebClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/latest")
                        .queryParam("base", "IDR")
                        .build())
                .retrieve()
                .bodyToMono(LatestRatesResponse.class)
                .doOnSuccess(r -> log.debug("Received latest rates for date: {}", r.getDate()))
                .doOnError(e -> log.error("Failed to fetch latest rates", e));
    }
}
