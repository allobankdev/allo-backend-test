package achlaq.co.allo_backend_test.external.frankfurter;

import achlaq.co.allo_backend_test.config.FrankfurterProperties;
import achlaq.co.allo_backend_test.external.frankfurter.dto.HistoricalRatesResponse;
import achlaq.co.allo_backend_test.external.frankfurter.dto.LatestRatesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class FrankfurterClient {

    private final WebClient webClient;
    private final FrankfurterProperties props;

    public LatestRatesResponse getLatestIdrRates() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/latest")
                        .queryParam("base", "IDR")
                        .build())
                .retrieve()
                .bodyToMono(LatestRatesResponse.class)
                .block();
    }

    public HistoricalRatesResponse getHistoricalIdrUsd() {
        return webClient.get()
                .uri("/" + props.getHistoricalRange() + "?from=IDR&to=USD")
                .retrieve()
                .bodyToMono(HistoricalRatesResponse.class)
                .block();
    }

    public Map<String, String> getCurrencies() {
        return webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .block();
    }
}

