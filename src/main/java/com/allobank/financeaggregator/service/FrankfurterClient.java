package com.allobank.financeaggregator.service;

import com.allobank.financeaggregator.exception.ExternalServiceException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class FrankfurterClient {

    private final WebClient webClient;

    public FrankfurterClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public <T> T get(String path, Class<T> responseType) {
        try {
            return webClient.get()
                    .uri(path)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(String.class)
                            .defaultIfEmpty("")
                            .map(body -> new ExternalServiceException(
                                    "Frankfurter API error: " + response.statusCode() + " " + body)))
                    .bodyToMono(responseType)
                    .block();
        } catch (WebClientResponseException ex) {
            throw new ExternalServiceException("Frankfurter API error: " + ex.getStatusCode(), ex);
        }
    }

    public <T> T get(String path, ParameterizedTypeReference<T> responseType) {
        try {
            return webClient.get()
                    .uri(path)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(String.class)
                            .defaultIfEmpty("")
                            .map(body -> new ExternalServiceException(
                                    "Frankfurter API error: " + response.statusCode() + " " + body)))
                    .bodyToMono(responseType)
                    .block();
        } catch (WebClientResponseException ex) {
            throw new ExternalServiceException("Frankfurter API error: " + ex.getStatusCode(), ex);
        }
    }
}
