package com.allobank.allobackendtest.strategy.impl;

import com.allobank.allobackendtest.exception.ExternalServiceException;
import com.allobank.allobackendtest.strategy.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("supported_currencies")
@RequiredArgsConstructor
@Slf4j
public class SupportedCurrenciesStrategy implements IDRDataFetcher {
    private final WebClient webClient;
    @Override
    public Mono<Object> fetchData() {
        return webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(Map.class)
                .onErrorMap(e -> !(e instanceof ExternalServiceException),
                        e -> new ExternalServiceException("API unavailable", e))
                .map(response -> {
                    List<Map<String, String>> resultList = new ArrayList<>();

                    response.forEach((code, name) -> {
                        resultList.add(Map.of(
                                "code", code.toString(),
                                "name", name.toString()
                        ));
                    });
                    return resultList;
                });
    }

    @Override
    public boolean supports(String type) {
        return "supported_currencies".equals(type);
    }
}
