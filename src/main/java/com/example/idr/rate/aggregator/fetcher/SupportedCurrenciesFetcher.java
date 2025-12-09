package com.example.idr.rate.aggregator.fetcher;

import com.example.idr.rate.aggregator.dto.CurrenciesDto;
import com.example.idr.rate.aggregator.exception.ExternalServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@RequiredArgsConstructor
@Component("supported_currencies")
public class SupportedCurrenciesFetcher implements IdrDataFetcher {
    private final WebClient webClient;

    @Override
    public Mono<Object> fetch() {
        return webClient.get()
                .uri("/currencies")
                .retrieve()
                .onStatus(s -> !s.is2xxSuccessful(), resp -> Mono.error(new ExternalServiceException("failed currencies")))
                .bodyToMono(Map.class)
                .map(map -> {
                    CurrenciesDto dto = new CurrenciesDto();
                    dto.setCurrencies((Map<String, String>) map);
                    return dto;
                });
    }
}
