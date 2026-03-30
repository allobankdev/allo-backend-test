package com.allobank.idrrates.strategy;

import com.allobank.idrrates.dto.LatestRatesDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class LatestRatesStrategy implements IdrDataFetcher {

    private static final Logger log = LoggerFactory.getLogger(LatestRatesStrategy.class);
    @Autowired
    private WebClient webClient;

    @Override
    public String getResourceType() {
        return "latest_idr_rates";

    }

    @Override
    public Object fetchData() {
        log.info("Fetching latest IDR rates from Frankfurter API");
        LatestRatesDTO dto = webClient.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .map(body -> new RuntimeException(
                                        "External API error:" + clientResponse.statusCode() + " - " + body
                                )))
                .bodyToMono(LatestRatesDTO.class)
                .onErrorMap(Exception.class,
                        ex -> new RuntimeException("Failed to fetch latest IDR rates", ex))
                .block();

        if (dto != null && dto.getRates() != null) {
            Double rateUsd = dto.getRates().get("USD");
            if (rateUsd != null && rateUsd != 0) {
                double spreadFactor = calculateSpreadFactor("farizmr");
                double usdBuySpreadIdr = (1.0 / rateUsd) * (1.0 + spreadFactor);
                dto.setUsdBuySpreadIdr(usdBuySpreadIdr);
                log.info("USD_BuySpread_IDR calculated: {}", usdBuySpreadIdr);
            }
        }

        return dto;
    }

    private double calculateSpreadFactor(String githubUsername) {
        int sum = githubUsername.toLowerCase()
                .chars()
                .sum();
        return (sum % 1000) / 100_000.0;
    }
}
