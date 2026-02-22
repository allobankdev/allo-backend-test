package com.allo.backendtest.strategy;

import com.allo.backendtest.dto.LatestRatesResponse;
import com.allo.backendtest.util.SpreadCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@Component("latest_idr_rates")
public class LatestIdrRatesFetcher implements IDRDataFetcher {

    private static final Logger log = LoggerFactory.getLogger(LatestIdrRatesFetcher.class);

    private final WebClient webClient;

    private final String githubUsername;

    public LatestIdrRatesFetcher(
            WebClient webClient,
            @Value("${frankfurter.github-username}") String githubUsername) {

        this.webClient = webClient;
        this.githubUsername = githubUsername;
    }

    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }

    @Override
    public List<Object> fetchAndTransform() {

        log.info("Fetching latest IDR rates...");

        LatestRatesResponse response =
                webClient.get()
                        .uri("/latest?base=IDR")
                        .retrieve()
                        .onStatus(status -> status.is4xxClientError(),
                                res -> res.bodyToMono(String.class)
                                        .map(body -> new RuntimeException("Client error: " + body)))
                        .onStatus(status -> status.is5xxServerError(),
                                res -> res.bodyToMono(String.class)
                                        .map(body -> new RuntimeException("Server error: " + body)))
                        .bodyToMono(LatestRatesResponse.class)
                        .block(Duration.ofSeconds(5));

        BigDecimal rateUsd = BigDecimal.valueOf(response.rates().get("USD"));

        BigDecimal spreadFactor = SpreadCalculator.calculateSpreadFactor(githubUsername);

        BigDecimal usdBuySpread = BigDecimal.ONE
                .divide(rateUsd, 8, RoundingMode.HALF_UP)
                .multiply(BigDecimal.ONE.add(spreadFactor));

        Map<String, Object> result = Map.of(
                "currency", "USD",
                "rate", rateUsd,
                "USD_BuySpread_IDR", usdBuySpread
        );

        return List.of(result);
    }
}
