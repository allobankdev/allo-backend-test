package com.allobank.idr_rate_aggregator.strategy;

import com.allobank.idr_rate_aggregator.config.FrankfurterProperties;
import com.allobank.idr_rate_aggregator.model.FinanceData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    private final WebClient webClient;
    private final FrankfurterProperties properties;

    @Override
    public FinanceData fetch() {
        log.info("Fetching supported currencies...");

        Map<String, Object> result = webClient.get()
                .uri(properties.getEndpoints().getCurrencies())
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .map(body -> new RuntimeException(
                                        "External API error: " + response.statusCode() + " - " + body)))
                .bodyToMono(new org.springframework.core.ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        return FinanceData.ofCurrencies(result);
    }

    @Override
    public String getResourceType() {
        return "supported_currencies";
    }
}
