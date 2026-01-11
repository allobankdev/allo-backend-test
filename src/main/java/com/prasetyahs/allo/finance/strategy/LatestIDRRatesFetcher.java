package com.prasetyahs.allo.finance.strategy;

import com.prasetyahs.allo.finance.model.EnhancedLatestData;
import com.prasetyahs.allo.finance.model.LatestResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component("latest_idr_rates")
public class LatestIDRRatesFetcher implements IDRDataFetcher {

    private final String githubUsername;

    public LatestIDRRatesFetcher(@Value("${app.github-username}") String githubUsername) {
        this.githubUsername = githubUsername;
    }

    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }

    @Override
    public Object fetchAndProcess(WebClient client) {
        LatestResponse response = client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/latest")
                        .queryParam("base", "IDR")
                        .build())
                .retrieve()
                .bodyToMono(LatestResponse.class)
                .block();

        if (response == null || !response.rates().containsKey("USD")) {
            throw new RuntimeException("Failed to fetch rates or USD rate missing");
        }

        double rateUsd = response.rates().get("USD");
        double spreadFactor = calculateSpreadFactor(githubUsername);
        double usdBuySpreadIdr = (1 / rateUsd) * (1 + spreadFactor);

        return new EnhancedLatestData(
                response.base(),
                response.date(),
                response.rates(),
                usdBuySpreadIdr);
    }

    private double calculateSpreadFactor(String username) {
        int sum = 0;
        for (char c : username.toLowerCase().toCharArray()) {
            sum += c;
        }
        return (sum % 1000) / 100000.0;
    }
}
