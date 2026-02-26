package com.api.allorestapi.client;

import com.api.allorestapi.model.HistoricalRatesResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class HistoricalRates implements Frankfurter<HistoricalRatesResponse> {

    private final WebClient frankfurterWebClient;

    @Value("${frankfurter.historical.start-date:2024-01-01}")
    private String startDate;

    @Value("${frankfurter.historical.end-date:2024-01-05}")
    private String endDate;

    @Value("${frankfurter.historical.from-currency:IDR}")
    private String fromCurrency;

    @Value("${frankfurter.historical.to-currency:USD}")
    private String toCurrency;

    @Override
    public Mono<HistoricalRatesResponse> fetch() {
        String path = String.format("/%s..%s", startDate, endDate);
        log.debug("Fetching historical rates: {} from {} to {}", path, fromCurrency, toCurrency);

        return frankfurterWebClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .queryParam("from", fromCurrency)
                        .queryParam("to", toCurrency)
                        .build())
                .retrieve()
                .bodyToMono(HistoricalRatesResponse.class)
                .doOnSuccess(r -> log.debug("Received historical rates from {} to {}",
                        r.getStartDate(), r.getEndDate()))
                .doOnError(e -> log.error("Failed to fetch historical rates", e));
    }
}
