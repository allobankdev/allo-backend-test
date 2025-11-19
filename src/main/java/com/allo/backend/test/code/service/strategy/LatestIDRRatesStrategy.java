package com.allo.backend.test.code.service.strategy;

import com.allo.backend.test.code.model.domain.LatestRatesData;
import com.allo.backend.test.code.model.dto.LatestRatesResponse;
import com.allo.backend.test.code.util.SpreadFactorCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class LatestIDRRatesStrategy implements DataFetcherStrategy {

    private final SpreadFactorCalculator spreadFactorCalculator;

    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }

    @Override
    public Object fetchData(WebClient webClient) {
        log.info("Fetching latest IDR rates from Frankfurter API");

        LatestRatesResponse response = webClient
                .get()
                .uri("/latest?base=IDR")
                .retrieve()
                .bodyToMono(LatestRatesResponse.class)
                .block();

        if (response == null) {
            throw new RuntimeException("Failed to fetch latest IDR rates");
        }

        // Calculate USD buy spread
        Double usdRate = response.getRates().get("USD");
        if (usdRate == null) {
            throw new RuntimeException("USD rate not found in response");
        }

        double usdBuySpread = spreadFactorCalculator.calculateUSDBySpread(usdRate);

        log.info("Calculated USD_BuySpread_IDR: {} (spread factor: {})",
                usdBuySpread, spreadFactorCalculator.getSpreadFactor());

        return LatestRatesData.builder()
                .amount(response.getAmount())
                .base(response.getBase())
                .date(response.getDate())
                .rates(response.getRates())
                .usdBuySpreadIDR(usdBuySpread)
                .spreadFactorNote(String.format("Calculated with spread factor %.5f for username: %s",
                        spreadFactorCalculator.getSpreadFactor(),
                        spreadFactorCalculator.getGithubUsername()))
                .build();
    }
}
