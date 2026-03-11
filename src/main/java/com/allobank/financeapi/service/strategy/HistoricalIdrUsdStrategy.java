package com.allobank.financeapi.service.strategy;

import com.allobank.financeapi.model.FinanceData;
import com.allobank.financeapi.model.FrankfurterHistoricalResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class HistoricalIdrUsdStrategy implements IDRDataFetcher {

    private final WebClient webClient;

    @Override
    public boolean supports(String resourceType) {
        return "historical_idr_usd".equals(resourceType);
    }

    @Override
    public String getResourceType() {
        return "historical_idr_usd";
    }

    @Override
    public Mono<FinanceData> fetchData() {
        log.debug("Fetching historical IDR to USD data from Frankfurter API");

        return webClient.get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve()
                .bodyToMono(FrankfurterHistoricalResponse.class)
                .map(response -> FinanceData.builder()
                        .resourceType(getResourceType())
                        .data(response.getRates())
                        .timestamp(LocalDate.now())
                        .build());
    }
}