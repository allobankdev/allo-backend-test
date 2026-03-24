package com.allo.test.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ExternalApiService {

    private final WebClient webClient;

    public ExternalApiService(WebClient webClient) {
        this.webClient = webClient;
    }

    public String testCall() {
        return webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}
