package com.vii.idragregator.strategy;

import com.vii.idragregator.dto.LatestRateDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

/**
 * @author Luthfi Aryarizki
 * @date Created on 2026/02/14 at 09:00 p.m
 */
@Slf4j
@Component("latest_idr_rates")
public class LatestRatesStrategy implements IDRDataFetcher{

    @Value("${app.github-username}")
    private String githubUsername;

    @Autowired
    private WebClient webClient;

    @Override
    public Object fetch() {
        log.info("Fetching latest rates from external API");
        LatestRateDTO dto = webClient.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .bodyToMono(LatestRateDTO.class)
                .block();

        if (dto != null && dto.getRates() != null && dto.getRates().containsKey("USD")) {
            double rateUsd = dto.getRates().get("USD");
            // Dynamic Spread Calculation
            int asciiSum = githubUsername.chars().sum();
            double spreadFactor = (asciiSum % 1000) / 100000.0;

            double buySpreadIdr = (1 / rateUsd) * (1 + spreadFactor);
            dto.setUsd_buySpread_idr(buySpreadIdr);
            log.info("Spread calculation completed for user: {} with factor: {}", githubUsername, spreadFactor);
        }
        return dto;
    }

}
