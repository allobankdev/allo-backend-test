package com.allobanktest.idr.strategy;

import com.allobanktest.idr.dto.CurrencyCatalog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

@Component("supported_currencies")
@RequiredArgsConstructor
public class SupportedCurrenciesStrategy implements IDRDataFetcher {

    private final WebClient webClient;

    @Override
    public String key() {
        return "supported_currencies";
    }

    @Override
    public Mono<Map<String, Object>> fetchData() {
        return webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(CurrencyCatalog.class)
                .map(dto -> {
                    Map<String, Object> result = new LinkedHashMap<>();
                    result.put("currencies", Map.copyOf(dto.getCurrencies()));
                    return Map.copyOf(result);
                })
                .onErrorResume(ex -> Mono.just(Map.of("error", ex.getMessage())));
    }
}
