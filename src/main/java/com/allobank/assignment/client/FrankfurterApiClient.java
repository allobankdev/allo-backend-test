package com.allobank.assignment.client;

import com.allobank.assignment.exception.ExternalServiceException;
import com.allobank.assignment.model.LatestRatesResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class FrankfurterApiClient {

    private final WebClient webClient;

    public FrankfurterApiClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public LatestRatesResponse getLatestRates(String base) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/latest").queryParam("base", base).build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(String.class)
                        .defaultIfEmpty("Frankfurter latest rates error")
                        .map(body -> new ExternalServiceException("Failed to fetch latest rates: " + body,
                                response.statusCode())))
                .bodyToMono(LatestRatesResponse.class)
                .blockOptional()
                .orElseThrow(() -> new ExternalServiceException("Frankfurter latest rates response was empty"));
    }
}
