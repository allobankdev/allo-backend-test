package com.allobanktest.idr.strategy;

import com.allobanktest.idr.dto.ExchangeRateTimeSeries;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component("historical_idr_usd")
@RequiredArgsConstructor
public class HistoricalIdrUsdStrategy implements IDRDataFetcher {

    private final WebClient webClient;

    @Override
    public String key() {
        return "historical_idr_usd";
    }

    @Override
    public Mono<Map<String, Object>> fetchData() {
        String path = "/2024-01-01..2024-01-05";
        return webClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path(path)
                                .queryParam("from", "IDR")
                                .queryParam("to", "USD")
                                .build())
                .retrieve()
                .bodyToMono(ExchangeRateTimeSeries.class)
                .map(this::toResult)
                .onErrorResume(ex -> Mono.just(Map.of("error", ex.getMessage())));
    }

    private Map<String, Object> toResult(ExchangeRateTimeSeries resp) {
        return Map.of(
                "amount", resp.getAmount(),
                "base", resp.getBase(),
                "startDate", resp.getStartDate(),
                "endDate", resp.getEndDate(),
                "rates", Map.copyOf(resp.getRates() == null ? Map.of() : resp.getRates())
        );
    }
}
