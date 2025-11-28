package com.project.alloBank.service;

import com.project.alloBank.dto.LatestRatesResponse;
import com.project.alloBank.repository.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class LatestIdrRatesFetcher implements IDRDataFetcher {

    private final WebClient webClient;

    @Value("${app.github.username}")
    private String githubUsername;

    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }

    @Override
    public Object fetchData() {
        LatestRatesResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/latest").queryParam("base", "IDR").build())
                .retrieve()
                .bodyToMono(LatestRatesResponse.class)
                .block();

        if (response == null) {
            throw new IllegalStateException("Failed to fetch latest rates");
        }

        Double usdRate = response.getRates().get("USD");
        if (usdRate == null) {
            throw new IllegalStateException("USD rate missing in API response");
        }

        // compute spread factor
        String username = githubUsername == null ? "" : githubUsername.toLowerCase();
        long sum = username.chars().sum();
        double spreadFactor = (sum % 1000) / 100000.0;

        double usdBuySpreadIdr = (1.0 / usdRate) * (1.0 + spreadFactor);

        response.setUsdBuySpreadIdr(usdBuySpreadIdr);
        return response;    }
}
