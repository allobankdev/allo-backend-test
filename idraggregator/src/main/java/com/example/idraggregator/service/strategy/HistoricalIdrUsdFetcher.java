package com.example.idraggregator.service.strategy;

import com.example.idraggregator.config.FrankfurterClientFactoryBean;
import com.example.idraggregator.dto.HistoricalDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Fetches historical time-series: /2024-01-01..2024-01-05?from=IDR&to=USD
 */
@Component("historical_idr_usd")
public class HistoricalIdrUsdFetcher implements IDRDataFetcher<HistoricalDto> {

    private final WebClient webClient;

    @Autowired
    public HistoricalIdrUsdFetcher(FrankfurterClientFactoryBean clientFactoryBean) {
        this.webClient = (WebClient) clientFactoryBean.getObject();
    }

    @Override
    public HistoricalDto fetch() throws Exception {
        Mono<HistoricalDto> mono = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/2024-01-01..2024-01-05")
                        .queryParam("from", "IDR")
                        .queryParam("to", "USD")
                        .build())
                .retrieve()
                .bodyToMono(HistoricalDto.class);

        HistoricalDto dto = mono.block();
        if (dto == null) throw new IllegalStateException("No historical payload");
        // Optionally set start_date and end_date, frankfurter typically returns "rates" with dates
        return dto;
    }

    @Override
    public String resourceKey() {
        return "historical_idr_usd";
    }
}
