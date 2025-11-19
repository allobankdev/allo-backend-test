package com.allobank.service;

import com.allobank.dto.response.GetHistoricalResponse;
import com.allobank.enums.RESPONSE;
import com.allobank.exceptions.BusinessException;
import com.allobank.exceptions.ExternalException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service("historical_idr_usd")
@RequiredArgsConstructor
public class GetHistoricalService implements IDRDataFetcher<GetHistoricalResponse> {

    private final WebClient webClient;

    @Override
    public Mono<GetHistoricalResponse> fetch() {
        return webClient.get()
                .uri("/2024-01-05?from=IDR&to=USD")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, r -> Mono.error(new BusinessException(RESPONSE.GENERAL_ERROR)))
                .onStatus(HttpStatusCode::is5xxServerError, r -> Mono.error(new ExternalException(RESPONSE.EXTERNAL_FAILED)))
                .bodyToMono(GetHistoricalResponse.class);
    }
}
