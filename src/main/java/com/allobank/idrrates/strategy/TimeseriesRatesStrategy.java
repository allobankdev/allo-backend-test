package com.allobank.idrrates.strategy;

import com.allobank.idrrates.dto.TimeseriesRatesDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class TimeseriesRatesStrategy implements IdrDataFetcher {

    private static final Logger log = LoggerFactory.getLogger(TimeseriesRatesStrategy.class);
    @Autowired
    private WebClient webClient;

    @Override
    public String getResourceType() {
        return "historical_idr_usd";
    }

    @Override
    public Object fetchData() {
        log.info("Fetching historical IDR/USD rates from Frankfurter API");
        return webClient.get()
                .uri("/2026-01-01..2026-02-01?from=IDR&to=USD")
                .retrieve()
                .bodyToMono(TimeseriesRatesDTO.class)
                .block();
    }
}
