package com.example.feat.idr_rate_aggregator.service.Latest;

import com.example.feat.idr_rate_aggregator.dto.LatestRatesResponse;
import com.example.feat.idr_rate_aggregator.exception.ExternalApiException;
import com.example.feat.idr_rate_aggregator.service.financeDataStore.IDRDataFetcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component("latest_idr_rates_fetcher")
public class LatestRatesFetcher implements IDRDataFetcher {

    private static final String RESOURCE_KEY = "latest_idr_rates";
    private final WebClient webClient;
    public final BigDecimal spreadFactor;

    public LatestRatesFetcher(WebClient webClient, @Value("${frankfurter.username}") String username) {
        this.webClient = webClient;
        this.spreadFactor = calculateSpreadFactor(username);
    }

    @Override
    public String getResourceKey() {
        return RESOURCE_KEY;
    }

    private BigDecimal calculateSpreadFactor(String username) {
        String lowerCaseUsername = username.toLowerCase();
        long sumOfUnicodeValues = lowerCaseUsername.chars().sum();
        double factorValue = (sumOfUnicodeValues % 1000) / 100000.0;
        return new BigDecimal(String.valueOf(factorValue));
    }

    @Override
    public Object fetchData() {
        LatestRatesResponse rawData = webClient.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class).flatMap(body ->
                                reactor.core.publisher.Mono.error(new ExternalApiException("Frankfurter API error: " + body))
                        ))
                .bodyToMono(LatestRatesResponse.class)
                .block();
        if (rawData == null || rawData.getRates() == null || !rawData.getRates().containsKey("USD")) {
            throw new ExternalApiException("Missing USD rate in Frankfurter response.");
        }

        BigDecimal rateUsd = rawData.getRates().get("USD");
        BigDecimal one = BigDecimal.ONE;

        BigDecimal invertedRate = one.divide(rateUsd, 6, RoundingMode.HALF_UP);

        BigDecimal spreadMultiplier = one.add(this.spreadFactor);

        BigDecimal usdBuySpreadIdr = invertedRate.multiply(spreadMultiplier)
                .setScale(4, RoundingMode.HALF_UP);

        rawData.setUSDBuySpreadIDR(usdBuySpreadIdr);

        return rawData;
    }
}
