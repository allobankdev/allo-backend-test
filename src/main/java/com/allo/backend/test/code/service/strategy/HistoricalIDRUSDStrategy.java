package com.allo.backend.test.code.service.strategy;

import com.allo.backend.test.code.model.domain.HistoricalRatesData;
import com.allo.backend.test.code.model.dto.HistoricalRatesResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
public class HistoricalIDRUSDStrategy implements DataFetcherStrategy {

    @Override
    public String getResourceType() {
        return "historical_idr_usd";
    }

    @Override
    public Object fetchData(WebClient webClient) {
        log.info("Fetching historical IDR to USD rates from Frankfurter API");

        HistoricalRatesResponse response = webClient
                .get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve()
                .bodyToMono(HistoricalRatesResponse.class)
                .block();

        if (response == null) {
            throw new RuntimeException("Failed to fetch historical IDR to USD rates");
        }

        log.info("Fetched historical data from {} to {}",
                response.getStartDate(), response.getEndDate());

        return HistoricalRatesData.builder()
                .amount(response.getAmount())
                .base(response.getBase())
                .startDate(response.getStartDate())
                .endDate(response.getEndDate())
                .rates(response.getRates())
                .build();
    }
}
