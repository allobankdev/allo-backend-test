package com.example.idr.rate.aggregator.fetcher;

import com.example.idr.rate.aggregator.dto.HistoricalCurrenciesDto;
import com.example.idr.rate.aggregator.exception.ExternalServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@RequiredArgsConstructor
@Component("historical_idr_usd")
public class HistoricalIdrUsdFetcher implements IdrDataFetcher {
    private final WebClient webClient;

    @Override
    public Mono<Object> fetch() {
        String path = "/2024-01-01..2024-01-05";
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(path)
                        .queryParam("from","IDR")
                        .queryParam("to","USD")
                        .build())
                .retrieve()
                .onStatus(s -> !s.is2xxSuccessful(), resp -> Mono.error(new ExternalServiceException("failed historical")))
                .bodyToMono(Map.class)
                .map(map -> {
                    HistoricalCurrenciesDto dto = new HistoricalCurrenciesDto();
                    dto.setStartDate("2024-01-01");
                    dto.setEndDate("2024-01-05");
                    dto.setRaw(map);
                    return dto;
                });
    }
}
