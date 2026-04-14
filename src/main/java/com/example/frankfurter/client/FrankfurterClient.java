package com.example.frankfurter.client;

import com.example.frankfurter.dto.FrankfurterLatestResponse;
import com.example.frankfurter.dto.FrankfurterTimeseriesResponse;
import com.example.frankfurter.exception.ExternalApiException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatusCode;


import java.time.LocalDate;
import java.util.Map;

@Component
public class FrankfurterClient {

    private final WebClient webClient;

    public FrankfurterClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public FrankfurterLatestResponse getLatestIdrRates() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/latest")
                        .queryParam("base", "IDR")
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .defaultIfEmpty("Unknown error")
                                .flatMap(body -> Mono.error(new ExternalApiException("Error from Frankfurter /latest: " + body)))
                )
                .bodyToMono(FrankfurterLatestResponse.class)
                .block();
    }

    public FrankfurterTimeseriesResponse getHistoricalIdrUsd(LocalDate start, LocalDate end) {
        String path = "/" + start + ".." + end;
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .queryParam("from", "IDR")
                        .queryParam("to", "USD")
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .defaultIfEmpty("Unknown error")
                                .flatMap(body -> Mono.error(new ExternalApiException("Error from Frankfurter timeseries: " + body)))
                )
                .bodyToMono(FrankfurterTimeseriesResponse.class)
                .block();
    }

        @SuppressWarnings("unchecked")
        public Map<String, String> getCurrencies() {
        return webClient.get()
                .uri("/currencies")
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .defaultIfEmpty("Unknown error")
                                .flatMap(body -> Mono.error(new ExternalApiException("Error from Frankfurter /currencies: " + body)))
                )
                .bodyToMono(Map.class)
                .block();
    }
}
