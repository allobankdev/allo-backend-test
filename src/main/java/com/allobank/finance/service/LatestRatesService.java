package com.allobank.finance.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.allobank.finance.client.FrankfurterClient;
import com.allobank.finance.dto.LatestRatesResponse;

/**
 * Service for fetching latest IDR rates from Frankfurter API
 */
@Service
public class LatestRatesService {

    private final String githubUsername;

    private final FrankfurterClient frankfurterClient;

    public LatestRatesService(FrankfurterClient frankfurterClient,
                              @org.springframework.beans.factory.annotation.Value("${app.github-username}") String githubUsername) {
        this.frankfurterClient = frankfurterClient;
        this.githubUsername = githubUsername;
    }

    /**
     * Fetches latest exchange rates for IDR and applies spread factor
     * @return LatestRatesResponse with USD buy spread rate
     */
    public LatestRatesResponse fetchLatestRates() {
        WebClient webClient = frankfurterClient.getWebClient();

        LatestRatesResponse response = webClient.get()
            .uri("/latest?base=IDR")
            .retrieve()
            .bodyToMono(LatestRatesResponse.class)
            .block();

        if (response != null && response.getRates() != null) {
            Double usdRate = response.getRates().get("USD");
            if (usdRate != null) {
                double spreadFactor = calculateSpreadFactor();
                double usdBuySpreadIdr = (1.0 / usdRate) * (1.0 + spreadFactor);
                response.setUsdBuySpreadIdr(usdBuySpreadIdr);
            }
        }

        return response;
    }

    /**
     * Calculates unique spread factor based on GitHub username
     * Formula: (Sum of Unicode Values % 1000) / 100000.0
     */
    private double calculateSpreadFactor() {
        long sum = githubUsername.toLowerCase()
            .chars()
            .sum();

        return (sum % 1000) / 100000.0;
    }
}
