package com.vii.idragregator.strategy;

import com.vii.idragregator.dto.HistoricalRateDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Luthfi Aryarizki
 * @date Created on 2026/02/14 at 09:00 p.m
 */
@Slf4j
@Component("historical_idr_usd")
public class HistoricalDataStrategy implements IDRDataFetcher {

    @Autowired
    private WebClient webClient;

    @Override
    public Object fetch() {
        log.info("Fetching historical data for 2024-01-01..2024-01-05...");
        return webClient.get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve()
                .bodyToMono(HistoricalRateDTO.class)
                .block();
    }
}