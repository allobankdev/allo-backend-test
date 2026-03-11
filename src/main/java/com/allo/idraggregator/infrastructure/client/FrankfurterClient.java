package com.allo.idraggregator.infrastructure.client;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

import com.allo.idraggregator.domain.model.Currency;
import com.allo.idraggregator.domain.model.HistoricalRates;
import com.allo.idraggregator.domain.model.LatestRates;
import com.allo.idraggregator.presentation.exception.ExternalApiException;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FrankfurterClient {

    private final WebClient webClient;

    public LatestRates getLatestRates(String base) {

        return getSpesification("/latest?base=" + base)
                .bodyToMono(LatestRates.class)
                .block();
    }

    public HistoricalRates getHistorical(String dateRange, String from, String to) {

        return getSpesification("/{dateRange}?from={from}&to={to}", dateRange, from, to)
                .bodyToMono(HistoricalRates.class)
                .block();
    }

    public Currency getCurrencies() {
        Map<String, String> response = getSpesification("/currencies")
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .block();
    
        return Currency.builder()
                .currencies(response)
                .build();
    }

    private ResponseSpec getSpesification(String uri, Object... uriVariables) {

        return webClient.get()
                .uri(uri, uriVariables)
                .retrieve()
                .onStatus(
                        status -> status.isError(),
                        response -> Mono.error(new ExternalApiException("Failed to call Frankfurter API")))
            ;
    }

}
