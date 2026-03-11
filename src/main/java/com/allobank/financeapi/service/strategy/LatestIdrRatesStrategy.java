package com.allobank.financeapi.service.strategy;

import com.allobank.financeapi.config.FrankfurterApiProperties;
import com.allobank.financeapi.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class LatestIdrRatesStrategy implements IDRDataFetcher {

    private final WebClient webClient;
    private final FrankfurterApiProperties properties;

    @Override
    public boolean supports(String resourceType) {
        return "latest_idr_rates".equals(resourceType);
    }

    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }

    @Override
    public Mono<FinanceData> fetchData() {
        log.debug("Fetching latest IDR rates from Frankfurter API");
        return webClient.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .bodyToMono(FrankfurterLatestResponse.class)
                .map(this::addSpreadCalculation)
                .map(data -> FinanceData.builder()
                        .resourceType(getResourceType())
                        .data(data)
                        .timestamp(LocalDate.now())
                        .build());
    }

    private List<LatestIdrWithSpread> addSpreadCalculation(FrankfurterLatestResponse response) {
        BigDecimal spreadFactor = calculateSpreadFactor();
        BigDecimal usdRate = response.getRates().get("USD");

        // Formula: USD_BuySpread_IDR = (1 / Rate_USD) * (1 + Spread Factor)
        BigDecimal inverseRate = BigDecimal.ONE.divide(usdRate, 10, RoundingMode.HALF_UP);
        BigDecimal usdWithSpread = inverseRate
                .multiply(BigDecimal.ONE.add(spreadFactor))
                .setScale(4, RoundingMode.HALF_UP);

        return response.getRates().entrySet().stream()
                .map(entry -> LatestIdrWithSpread.builder()
                        .currency(entry.getKey())
                        .originalRate(entry.getValue())
                        .usdBuySpreadIdr("USD".equals(entry.getKey()) ? usdWithSpread : null)
                        .spreadFactor("USD".equals(entry.getKey()) ? spreadFactor : null)
                        .date(response.getDate())
                        .build())
                .collect(Collectors.toList());
    }

    private BigDecimal calculateSpreadFactor() {
        // Calculate unique spread factor from GitHub username
        String username = properties.getGithubUsername().toLowerCase();
        int sum = username.chars().sum();
        double factor = (sum % 1000) / 100000.0;
        return BigDecimal.valueOf(factor).setScale(5, RoundingMode.HALF_UP);
    }
}