package com.allo.app.service.impl;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.allo.app.dto.FrankfurterProperties;
import com.allo.app.service.IDRDataFetcher;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SupportedCurrencies implements IDRDataFetcher<Map<String, String>>{

    private final WebClient webClient;
    private final FrankfurterProperties frankfurterProperties;

    @Override
    public Mono<Map<String, String>> getData() {
        return webClient.get()
                .uri(frankfurterProperties.getUrl() + "/currencies")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {});
    }

}
