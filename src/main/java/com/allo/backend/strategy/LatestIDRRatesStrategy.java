package com.allo.backend.strategy;

import com.allo.backend.exception.ExternalApiException;
import com.allo.backend.model.LatestRates;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class LatestIDRRatesStrategy implements IDRDataFetcher {

    private final WebClient webClient;

    @Value("${github.username}")
    private String githubUsername;

    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }

    @Override
    public Object fetchData() {
        log.info("Fetching latest IDR rates from external API");

        try {
            LatestRates latestRates = webClient.get()
                    .uri("/latest?base=IDR")
                    .retrieve()
                    .bodyToMono(LatestRates.class)
                    .onErrorResume(e -> {
                        log.error("Error fetching latest rates: {}", e.getMessage());
                        return Mono.error(new ExternalApiException("Failed to fetch latest rates", e));
                    })
                    .block();

            if (latestRates == null) {
                throw new ExternalApiException("Received null response from external API");
            }

            calculateUsdBuySpread(latestRates);

            log.info("Successfully fetched and processed latest IDR rates");
            return latestRates;

        } catch (Exception e) {
            log.error("Failed to fetch latest IDR rates: {}", e.getMessage(), e);
            throw new ExternalApiException("Failed to fetch latest IDR rates", e);
        }
    }

    private void calculateUsdBuySpread(LatestRates latestRates) {
        double spreadFactor = calculateSpreadFactor(githubUsername);
        log.debug("Calculated spread factor: {} for username: {}", spreadFactor, githubUsername);

        Map<String, Double> rates = latestRates.getRates();
        if (rates != null && rates.containsKey("USD")) {
            double usdRate = rates.get("USD");
            double usdBuySpreadIdr = (1 / usdRate) * (1 + spreadFactor);
            latestRates.setUsdBuySpreadIdr(usdBuySpreadIdr);
            log.debug("USD rate: {}, USD_BuySpread_IDR: {}", usdRate, usdBuySpreadIdr);
        } else {
            log.warn("USD rate not found in response, cannot calculate USD_BuySpread_IDR");
        }
    }

    public double calculateSpreadFactor(String username) {
        String lowercaseUsername = username.toLowerCase();
        int sum = 0;

        for (char c : lowercaseUsername.toCharArray()) {
            sum += (int) c;
        }

        double spreadFactor = (sum % 1000) / 100000.0;
        log.info("Spread factor calculation - Username: {}, Unicode sum: {}, Spread factor: {}",
                username, sum, spreadFactor);

        return spreadFactor;
    }
}
