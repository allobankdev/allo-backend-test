package com.musfiul.idrrateaggregator.client;

import com.musfiul.idrrateaggregator.dto.CurrenciesResponse;
import com.musfiul.idrrateaggregator.dto.HistoricalRatesResponse;
import com.musfiul.idrrateaggregator.dto.LatestRatesResponse;
import com.musfiul.idrrateaggregator.exception.ExternalApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class FrankfurterClientImpl implements FrankfurterClient {
    private static final String CLIENT_ERROR = "Client error from Frankfurter API";
    private static final String SERVER_ERROR = "Server error from Frankfurter API";
    private static final String FAILED_CALL = "Failed to call Frankfurter API";
    private final WebClient webClient;


    public FrankfurterClientImpl(WebClient.Builder builder,
                                 @Value("${frankfurter.api.url}") String baseUrl) {

        this.webClient = builder
                .baseUrl(baseUrl)
                .build();
    }

    @Override
    public LatestRatesResponse getLatestRates() {

        return webClient.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class)
                                .defaultIfEmpty(CLIENT_ERROR)
                                .map(ExternalApiException::new)
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class)
                                .defaultIfEmpty(SERVER_ERROR)
                                .map(ExternalApiException::new)
                )
                .bodyToMono(LatestRatesResponse.class)
                .onErrorMap(
                        ex -> !(ex instanceof ExternalApiException),
                        ex -> new ExternalApiException(FAILED_CALL, ex)
                )
                .block();
    }

    @Override
    public HistoricalRatesResponse getHistoricalRates() {

        return webClient.get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class)
                                .defaultIfEmpty(CLIENT_ERROR)
                                .map(ExternalApiException::new)
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class)
                                .defaultIfEmpty(SERVER_ERROR)
                                .map(ExternalApiException::new)
                )
                .bodyToMono(HistoricalRatesResponse.class)
                .onErrorMap(
                        ex -> !(ex instanceof ExternalApiException),
                        ex -> new ExternalApiException(FAILED_CALL, ex)
                )
                .block();
    }

    @Override
    public CurrenciesResponse getCurrencies() {

        return webClient.get()
                .uri("/currencies")
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class)
                                .defaultIfEmpty(CLIENT_ERROR)
                                .map(ExternalApiException::new)
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class)
                                .defaultIfEmpty(SERVER_ERROR)
                                .map(ExternalApiException::new)
                )
                .bodyToMono(CurrenciesResponse.class)
                .onErrorMap(
                        ex -> !(ex instanceof ExternalApiException),
                        ex -> new ExternalApiException(FAILED_CALL, ex)
                )
                .block();
    }
}
