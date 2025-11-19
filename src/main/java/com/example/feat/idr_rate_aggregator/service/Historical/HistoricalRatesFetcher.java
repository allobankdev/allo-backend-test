package com.example.feat.idr_rate_aggregator.service.Historical;

import com.example.feat.idr_rate_aggregator.dto.HistoricalRatesResponse;
import com.example.feat.idr_rate_aggregator.exception.ExternalApiException;
import com.example.feat.idr_rate_aggregator.service.financeDataStore.IDRDataFetcher;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component("historical_idr_usd")
public class HistoricalRatesFetcher implements IDRDataFetcher {

    private static final String RESOURCE_KEY = "historical_idr_usd";
    private static final String DATE_RANGE_URI = "/2024-01-01..2024-01-05?from=IDR&to=USD";
    private final WebClient webClient;

    public HistoricalRatesFetcher(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String getResourceKey() {
        return RESOURCE_KEY;
    }

    @Override
    public Object fetchData() {
        return webClient.get()
                .uri(DATE_RANGE_URI)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class).flatMap(body ->
                                reactor.core.publisher.Mono.error(new ExternalApiException("Frankfurter API 4xx/5xx error: " + body))
                        ))
                .bodyToMono(HistoricalRatesResponse.class)
                .block();
    }
}