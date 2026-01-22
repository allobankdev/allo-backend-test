package com.allobank.idr_rate_aggregator.service.strategy;

import com.allobank.idr_rate_aggregator.config.FrankfurterApiProperties;
import com.allobank.idr_rate_aggregator.model.dto.FrankfurterTimeSeriesResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class HistoricalIDRUSDStrategy implements IDRDataFetcherStrategy {

    private final WebClient webClient;
    private final FrankfurterApiProperties properties;

    @Override
    public Object fetchData() {
        log.info("Fetching historical IDR to USD rates");
        
        try {
            String startDate = properties.getHistorical().getStartDate();
            String endDate = properties.getHistorical().getEndDate();
            
            String uri = String.format("/%s..%s?from=IDR&to=USD", startDate, endDate);
            
            FrankfurterTimeSeriesResponse response = webClient.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(FrankfurterTimeSeriesResponse.class)
                    .block();
            
            if (response == null) {
                throw new RuntimeException("Received null response from Frankfurter API");
            }
            
            log.info("Successfully fetched historical data from {} to {}", startDate, endDate);
            return response;
            
        } catch (Exception e) {
            log.error("Error fetching historical IDR to USD rates", e);
            throw new RuntimeException("Failed to fetch historical data: " + e.getMessage(), e);
        }
    }

    @Override
    public String getResourceType() {
        return "historical_idr_usd";
    }
}
