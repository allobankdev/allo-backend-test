package com.allobank.idr_rate_aggregator.service.strategy;

import com.allobank.idr_rate_aggregator.model.ResourceType;
import com.allobank.idr_rate_aggregator.model.dto.CurrenciesResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class SupportedCurrenciesStrategy implements IDRDataFetcherStrategy {

    private final WebClient webClient;

    public SupportedCurrenciesStrategy(WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * Mendukung resource SUPPORTED_CURRENCIES
     */
    @Override
    public ResourceType getSupportedType() {
        return ResourceType.SUPPORTED_CURRENCIES;
    }

    /**
     * Mengambil daftar mata uang yang didukung
     */
    @Override
    public Object fetchData() {
        return webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(CurrenciesResponse.class)
                .block();
    }
}