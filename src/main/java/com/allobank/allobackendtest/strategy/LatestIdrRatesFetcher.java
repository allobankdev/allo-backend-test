package com.allobank.allobackendtest.strategy;

import com.allobank.allobackendtest.dto.LatestRatesResponse;
import com.allobank.allobackendtest.util.SpreadFactorCalculator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Component
public class LatestIdrRatesFetcher implements IdrDataFetcher {

    private final WebClient webClient;
    private final String githubUsername;

    public LatestIdrRatesFetcher(
            WebClient webClient,
            @Value("${frankfurter.github-username}") String githubUsername) {
        this.webClient = webClient;
        this.githubUsername = githubUsername;
    }

    @Override
    public String resourceType() {
        return "latest_idr_rates";
    }

    @Override
    public Object fetchFromApi() {
        // bentuk respons mentah Frankfurter
        record RawLatest(String base, String date, Map<String, BigDecimal> rates) {}

        RawLatest raw = webClient.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .bodyToMono(RawLatest.class)
                .block();

        if (raw == null || raw.rates() == null || !raw.rates().containsKey("USD")) {
            throw new IllegalStateException("USD rate not found in latest IDR rates");
        }

        BigDecimal rateUsd = raw.rates().get("USD"); // Rate_USD dari soal
        double spreadFactor = SpreadFactorCalculator.calculateSpreadFactor(githubUsername);

        // 1 / Rate_USD => IDR per 1 USD
        BigDecimal idrPerUsd = BigDecimal.ONE
                .divide(rateUsd, 8, BigDecimal.ROUND_HALF_UP);

        BigDecimal usdBuySpreadIdr = idrPerUsd
                .multiply(BigDecimal.valueOf(1.0 + spreadFactor));

        return new LatestRatesResponse(
                resourceType(),
                raw.base(),
                LocalDate.parse(raw.date()),
                raw.rates(),
                BigDecimal.valueOf(spreadFactor),
                usdBuySpreadIdr
        );
    }
}
