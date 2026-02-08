package com.allobank.test.strategy;

import com.allobank.test.config.FrankfurterProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class HistoricalIdrUsdStrategy implements IdrDataFetcher {

    private final WebClient webClient;
    private final FrankfurterProperties properties;

    @Value("${app.finance.historical-range}")
    private String dateRange;

    @Override
    public String getResourceType() {
        return "historical_idr_usd";
    }

    @Override
    public CompletableFuture<?> fetchData() {
        String path = properties.getEndpoints().getHistorical()
                .replace("{range}", dateRange);

        return webClient.get()
                .uri(path)
                .retrieve()
                .bodyToMono(Object.class)
                .toFuture();
    }
}
