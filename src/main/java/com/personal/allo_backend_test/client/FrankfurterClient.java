package com.personal.allo_backend_test.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.personal.allo_backend_test.client.response.HistoricalRatesResponse;
import com.personal.allo_backend_test.client.response.LatestRatesResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class FrankfurterClient {

  private final WebClient webClient;

  public Mono<LatestRatesResponse> fetchLatestRates() {
    return webClient.get()
      .uri(uriBuilder -> uriBuilder
        .path("/latest")
        .queryParam("base", "IDR")
        .build())
      .retrieve()
      .bodyToMono(new ParameterizedTypeReference<LatestRatesResponse>() {})
      .defaultIfEmpty(LatestRatesResponse.builder().build())
      .onErrorResume(error -> {
        log.error("Error fetching latest rates {}", error.getMessage(), error);
        return Mono.just(LatestRatesResponse.builder().build());
      });
  }

  public Mono<HistoricalRatesResponse> fetchHistoricalRates(String startDate, String endDate) {
    return webClient.get()
      .uri(uriBuilder -> uriBuilder
        .path("/{startDate}..{endDate}")
        .queryParam("from", "IDR")
        .queryParam("to", "USD")
        .build(startDate, endDate))
      .retrieve()
      .bodyToMono(new ParameterizedTypeReference<HistoricalRatesResponse>() {})
      .defaultIfEmpty(HistoricalRatesResponse.builder().build())
      .onErrorResume(error -> {
        log.error("Error fetching historical rates {}", error.getMessage(), error);
        return Mono.just(HistoricalRatesResponse.builder().build());
      });
  }

  public Mono<Map<String, Double>> fetchCurrencies() {
    return webClient.get()
      .uri("/currencies")
      .retrieve()
      .bodyToMono(new ParameterizedTypeReference<Map<String, Double>>() {})
      .defaultIfEmpty(new HashMap<>())
      .onErrorResume(error -> {
        log.error("Error fetching currencies {}", error.getMessage(), error);
        return Mono.just(new HashMap<>());
      });
  }
}
