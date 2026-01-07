package com.allobank.aggregator.strategy;

import com.allobank.aggregator.dto.FinanceDataDto;
import com.allobank.aggregator.dto.HistoricalRatesResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Component
@Qualifier("historical_idr_usd")
public class HistoricalIdrUsdFetcher implements IDRDataFetcher {

    private final WebClient webClient;

    public HistoricalIdrUsdFetcher(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String resourceKey() {
        return "historical_idr_usd";
    }

    @Override
    public FinanceDataDto fetch() {
        HistoricalRatesResponse resp = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/2024-01-01..2024-01-05")
                        .queryParam("from", "IDR")
                        .queryParam("to", "USD")
                        .build())
                .retrieve()
                .onStatus(s -> s.is4xxClientError() || s.is5xxServerError(),
                        clientResponse -> clientResponse.createException().flatMap(ex -> reactor.core.publisher.Mono.error(new RuntimeException("Frankfurter returned error: " + clientResponse.statusCode()))))
                .bodyToMono(HistoricalRatesResponse.class)
                .block();

        if (resp == null) {
            throw new RuntimeException("Empty historical response from Frankfurter");
        }

        Map<String, Object> out = new HashMap<>();
        out.put("range", "2024-01-01..2024-01-05");
        out.put("rates", resp.rates());
        return new FinanceDataDto(resourceKey(), out);
    }
}
