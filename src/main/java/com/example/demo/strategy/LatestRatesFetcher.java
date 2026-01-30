package com.example.demo.strategy;

import com.example.demo.dto.LatestResponse;
import com.example.demo.dto.LatestResult;
import com.example.demo.util.SpreadCalculator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class LatestRatesFetcher implements IDRDataFetcher {

    private final WebClient client;

    public LatestRatesFetcher(WebClient client) {
        this.client = client;
    }

    @Override
    public String getType() {
        return "latest_idr_rates";
    }

    @Override
    public List<?> fetchData() {

        LatestResponse response = client.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .bodyToMono(LatestResponse.class)
                .blockOptional()
                .orElseThrow(() -> new RuntimeException("Failed to fetch latest rates"));

        Double usdRate = response.getRates().get("USD");
        if (usdRate == null) {
            throw new RuntimeException("USD rate not found");
        }

        double spreadFactor = SpreadCalculator.calculate("raihan2-byte");
        double usdBuySpread = (1 / usdRate) * (1 + spreadFactor);

        return List.of(new LatestResult(response, usdBuySpread));
    }
}

