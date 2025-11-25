package com.example.financedata.fetcher;

import com.example.financedata.dto.CurrenciesDto;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    private final WebClient webClient;

    public SupportedCurrenciesFetcher(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<Object> fetch() {
        return webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(Map.class)
                .map(map -> {
                    CurrenciesDto dto = new CurrenciesDto();
                    dto.setCurrencies((Map<String, String>) map);
                    return dto;
                });
    }

    @Override
    public String resourceKey() {
        return "supported_currencies";
    }
}
