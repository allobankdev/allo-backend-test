package com.example.allotest.strategy;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component("supported_currencies")
public class SupportedCurrenciesFetcher implements IDataFetcher{
    private final WebClient webClient;

    public SupportedCurrenciesFetcher(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Object[] fetchData() {
        return new Object[]{webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(Object.class)
                .block()};
    }
}
