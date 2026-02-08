package com.allobank.test.strategy;

import com.allobank.test.config.FrankfurterProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class SupportedCurrenciesStrategy implements IdrDataFetcher {

    private final WebClient webClient;
    private final FrankfurterProperties properties;

    @Override
    public String getResourceType() {
        return "supported_currencies";
    }

    @Override
    public CompletableFuture<?> fetchData() {
        return webClient.get()
                .uri(properties.getEndpoints().getCurrencies())
                .retrieve()
                .bodyToMono(Object.class)
                .toFuture();
    }
}