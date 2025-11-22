package com.personal.allo_backend_test.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.personal.allo_backend_test.client.response.HistoricalRatesResponse;
import com.personal.allo_backend_test.client.response.LatestRatesResponse;
import com.personal.allo_backend_test.properties.FrankfurterClientProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class FrankfurterClient {

  private final FrankfurterClientProperties frankfurterClientProperties;

  private final WebClient webClient;

  public Mono<LatestRatesResponse> fetchLatestRates() {
    return webClient.get()
      .uri(uriBuilder -> uriBuilder
        .path("/latest")
        .queryParam("base", frankfurterClientProperties.getRate().getFrom())
        .build())
      .retrieve()
      .bodyToMono(new ParameterizedTypeReference<LatestRatesResponse>() {})
      .defaultIfEmpty(new LatestRatesResponse(null, null, null, null))
      .onErrorResume(error -> {
        log.error("Error fetching latest rates {}", error.getMessage(), error);
        return Mono.just(new LatestRatesResponse(null, null, null, null));
      });
  }

  public Mono<HistoricalRatesResponse> fetchHistoricalRates() {
    return webClient.get()
      .uri(uriBuilder -> uriBuilder
        .path("/{startDate}..{endDate}")
        .queryParam("from", frankfurterClientProperties.getRate().getFrom())
        .queryParam("to", frankfurterClientProperties.getRate().getTo())
        .build(frankfurterClientProperties.getRate().getHistoricalStartDate(),
          frankfurterClientProperties.getRate().getHistoricalEndDate()))
      .retrieve()
      .bodyToMono(new ParameterizedTypeReference<HistoricalRatesResponse>() {})
      .defaultIfEmpty(new HistoricalRatesResponse(null, null, null, null, null))
      .onErrorResume(error -> {
        log.error("Error fetching historical rates {}", error.getMessage(), error);
        return Mono.just(new HistoricalRatesResponse(null, null, null, null, null));
      });
  }

  public Mono<Map<String, String>> fetchCurrencies() {
    return webClient.get()
      .uri("/currencies")
      .retrieve()
      .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
      .defaultIfEmpty(new HashMap<>())
      .onErrorResume(error -> {
        log.error("Error fetching currencies {}", error.getMessage(), error);
        return Mono.just(new HashMap<>());
      });
  }
}
