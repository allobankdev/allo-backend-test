package com.nurmaya.allobank.idr_rate_aggregator.client;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.nurmaya.allobank.idr_rate_aggregator.configuration.FrankfurterProperties;
import com.nurmaya.allobank.idr_rate_aggregator.dto.CurrencyListResponse;
import com.nurmaya.allobank.idr_rate_aggregator.dto.HistoricalRatesResponse;
import com.nurmaya.allobank.idr_rate_aggregator.dto.LatestRatesResponse;

@Component
public class FrankfurterClient {
        private final WebClient webClient;
    private final FrankfurterProperties properties;

    public FrankfurterClient(WebClient webClient, FrankfurterProperties properties) {
        this.webClient = webClient;
        this.properties = properties;
    }

    public LatestRatesResponse getLatestIdrRates() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/latest")
                        .queryParam("base", "IDR")
                        .build()
                )
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                    response.bodyToMono(String.class)
                        .map(body -> new RuntimeException("Frankfurter API error: " + body))
                )
                .bodyToMono(LatestRatesResponse.class)
                .block();
    }

    public HistoricalRatesResponse getHistoricalIdrUsd() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/" + properties.getHistoricalRange())
                        .queryParam("from", "IDR")
                        .queryParam("to", "USD")
                        .build()
                )
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                    response.bodyToMono(String.class)
                        .map(body -> new RuntimeException("Frankfurter API error: " + body))
                )
                .bodyToMono(HistoricalRatesResponse.class)
                .block();
    }

    // public CurrencyListResponse getSupportedCurrencies() {
    //     return webClient.get()
    //             .uri("/currencies")
    //             .retrieve()
    //             .bodyToMono(CurrencyListResponse.class)
    //             .block();
    // }

    public CurrencyListResponse getSupportedCurrencies() {
        Map<String, String> responseMap = webClient.get()
                .uri("/currencies")
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                    response.bodyToMono(String.class)
                        .map(body -> new RuntimeException("Frankfurter API error: " + body))
                )
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .block();

        CurrencyListResponse response = new CurrencyListResponse();
        response.setCurrencies(responseMap);
        return response;
    }
}
