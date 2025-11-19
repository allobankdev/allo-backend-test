package com.allobank.service;

import com.allobank.exceptions.BusinessException;
import com.allobank.exceptions.ExternalException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.allobank.enums.RESPONSE.EXTERNAL_FAILED;
import static com.allobank.enums.RESPONSE.GENERAL_ERROR;

@Service("supported_currencies")
@RequiredArgsConstructor
public class GetSupportedCurrencyService implements IDRDataFetcher<Map<String, String>>{

    private final WebClient webClient;

    @Override
    public Mono<Map<String, String>> fetch() {
        return webClient.get()
                .uri("/currencies")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, r -> Mono.error(new BusinessException(GENERAL_ERROR)))
                .onStatus(HttpStatusCode::is5xxServerError, r -> Mono.error(new ExternalException(EXTERNAL_FAILED)))
                .bodyToMono(new ParameterizedTypeReference<>() {
                });
    }
}
