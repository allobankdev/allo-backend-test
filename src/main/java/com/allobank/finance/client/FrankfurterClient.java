package com.allobank.finance.client;

import org.springframework.web.reactive.function.client.WebClient;

public class FrankfurterClient {
    private final WebClient webClient;

    public FrankfurterClient(WebClient webClient) {
        this.webClient = webClient;
    }
}
